package iuh.fit.backend.services;

import iuh.fit.backend.enums.SkillLevel;
import iuh.fit.backend.models.*;
import iuh.fit.fontend.dto.JobRecommendation;
import lombok.Getter;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.nn.workspace.LayerWorkspaceMgr;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class JobRecommendationSystem {
    private MultiLayerNetwork model;
    private Map<Long, Integer> skillIndexMap;
    private int numFeatures;

    public void initializeModel(List<Skill> allSkills) {
        // Create skill to index mapping
        skillIndexMap = new HashMap<>();
        for (int i = 0; i < allSkills.size(); i++) {
            skillIndexMap.put(allSkills.get(i).getId(), i);
        }
        numFeatures = allSkills.size();

        // Build neural network configuration
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(123)
                .updater(new Adam(0.01))
                .weightInit(WeightInit.XAVIER)
                .list()
                .layer(0, new DenseLayer.Builder()
                        .nIn(numFeatures)
                        .nOut(32)
                        .activation(Activation.RELU)
                        .build())
                .layer(1, new DenseLayer.Builder()
                        .nIn(32)
                        .nOut(16)
                        .activation(Activation.RELU)
                        .build())
                .layer(2, new OutputLayer.Builder(LossFunctions.LossFunction.RECONSTRUCTION_CROSSENTROPY)
                        .nIn(16)
                        .nOut(numFeatures)
                        .activation(Activation.SIGMOID)
                        .build())
                .build();

        model = new MultiLayerNetwork(conf);
        model.init();
    }

    public void trainModel(List<Candidate> candidates, List<Job> jobs) {
        List<INDArray> trainingData = new ArrayList<>();

        // Create training data from both candidates and jobs
        for (Candidate candidate : candidates) {
            trainingData.add(createSkillVector(candidate.getCandidateSkills()));
        }

        for (Job job : jobs) {
            trainingData.add(createSkillVector(job.getJobSkills()));
        }

        // Convert to training matrix
        INDArray input = Nd4j.vstack(trainingData);

        // Train the model
        for (int i = 0; i < 100; i++) {  // 100 epochs
            model.fit(input, input);  // Autoencoder training (input = output)
        }
    }

    private INDArray createSkillVector(List<? extends Object> skills) {
        double[] vector = new double[numFeatures];

        if (skills.get(0) instanceof CandidateSkill) {
            List<CandidateSkill> candidateSkills = (List<CandidateSkill>) skills;
            for (CandidateSkill skill : candidateSkills) {
                int index = skillIndexMap.get(skill.getSkill().getId());
                vector[index] = normalizeSkillLevel(skill.getSkillLevel());
            }
        } else {
            List<JobSkill> jobSkills = (List<JobSkill>) skills;
            for (JobSkill skill : jobSkills) {
                int index = skillIndexMap.get(skill.getSkill().getId());
                vector[index] = normalizeSkillLevel(skill.getSkillLevel());
            }
        }

        return Nd4j.create(vector);
    }

    private double normalizeSkillLevel(SkillLevel level) {
        return level.getValue() / 5.0;  // Normalize to [0,1]
    }

    public List<JobRecommendation> recommendJobs(Candidate candidate, List<Job> allJobs, int topN) {
        INDArray candidateVector = createSkillVector(candidate.getCandidateSkills()).reshape(1, numFeatures);

        List<JobRecommendation> recommendations = new ArrayList<>();

        for (Job job : allJobs) {
            INDArray jobVector = createSkillVector(job.getJobSkills()).reshape(1, numFeatures);

            // Sử dụng feedForward để lấy encoding
            INDArray candidateEncoding = model.feedForward(candidateVector, false).get(1); // Encoding của ứng viên
            INDArray jobEncoding = model.feedForward(jobVector, false).get(1); // Encoding của công việc

            double similarity = cosineSimilarity(candidateEncoding, jobEncoding);

            recommendations.add(new JobRecommendation(job, similarity));
        }

        // Sort by similarity score and get top N
        return recommendations.stream()
                .sorted((r1, r2) -> Double.compare(r2.getScore(), r1.getScore()))
                .limit(topN)
                .collect(Collectors.toList());
    }



    private double cosineSimilarity(INDArray vec1, INDArray vec2) {
        double dotProduct = vec1.mul(vec2).sumNumber().doubleValue();
        double norm1 = Math.sqrt(vec1.mul(vec1).sumNumber().doubleValue());
        double norm2 = Math.sqrt(vec2.mul(vec2).sumNumber().doubleValue());
        return dotProduct / (norm1 * norm2);
    }
    public static void main(String[] args) {
        // Tạo danh sách các kỹ năng
        Skill skill1 = new Skill(1L, "Java");
        Skill skill2 = new Skill(2L, "Python");
        Skill skill3 = new Skill(3L, "SQL");
        Skill skill4 = new Skill(4L, "React");
        Skill skill5 = new Skill(5L, "Spring Boot");
        Skill skill6 = new Skill(6L, "Docker");

        List<Skill> allSkills = Arrays.asList(skill1, skill2, skill3, skill4, skill5, skill6);

        // Tạo danh sách ứng viên
        Candidate candidate1 = new Candidate(1L, "Alice", Arrays.asList(
                new CandidateSkill(skill1, SkillLevel.MASTER)
//                new CandidateSkill(skill2, SkillLevel.IMTERMEDIATE),
//                new CandidateSkill(skill3, SkillLevel.BEGINER)
        ));

        Candidate candidate2 = new Candidate(2L, "Bob", Arrays.asList(
                new CandidateSkill(skill3, SkillLevel.ADVANCED),
                new CandidateSkill(skill4, SkillLevel.IMTERMEDIATE),
                new CandidateSkill(skill6, SkillLevel.BEGINER)
        ));

        Candidate candidate3 = new Candidate(3L, "Charlie", Arrays.asList(
                new CandidateSkill(skill2, SkillLevel.BEGINER),
                new CandidateSkill(skill5, SkillLevel.MASTER),
                new CandidateSkill(skill6, SkillLevel.IMTERMEDIATE)
        ));

        List<Candidate> candidates = Arrays.asList(candidate1, candidate2, candidate3);

        // Tạo danh sách công việc
        Job job1 = new Job(1L, "Backend Developer", Arrays.asList(
                new JobSkill(skill1, SkillLevel.ADVANCED),
                new JobSkill(skill3, SkillLevel.ADVANCED),
                new JobSkill(skill5, SkillLevel.MASTER)
        ));

        Job job2 = new Job(2L, "Frontend Developer", Arrays.asList(
                new JobSkill(skill4, SkillLevel.MASTER),
                new JobSkill(skill3, SkillLevel.IMTERMEDIATE),
                new JobSkill(skill2, SkillLevel.BEGINER)
        ));

        Job job3 = new Job(3L, "DevOps Engineer", Arrays.asList(
                new JobSkill(skill6, SkillLevel.MASTER),
                new JobSkill(skill5, SkillLevel.IMTERMEDIATE),
                new JobSkill(skill1, SkillLevel.PROFESSIONAL)
        ));

        List<Job> jobs = Arrays.asList(job1, job2, job3);

        // Tạo hệ thống đề xuất công việc và khởi tạo mô hình
        JobRecommendationSystem recommendationSystem = new JobRecommendationSystem();
        recommendationSystem.initializeModel(allSkills);

        // Huấn luyện mô hình với dữ liệu (ứng viên và công việc)
        recommendationSystem.trainModel(candidates, jobs);

        // Đưa ra các khuyến nghị công việc cho từng ứng viên
        for (Candidate candidate : candidates) {
            List<JobRecommendation> recommendedJobs = recommendationSystem.recommendJobs(candidate, jobs, 2); // Top 2 công việc

            // In ra kết quả
            System.out.println("Top recommended jobs for " + candidate.getFullName() + ":");
            for (JobRecommendation recommendation : recommendedJobs) {
                System.out.println("- Job: " + recommendation.getJob().getName() +
                        " with similarity score: " + recommendation.getScore());
            }
            System.out.println();
        }
    }


}

//@Getter
//class JobRecommendation {
//    private Job job;
//    private double score;
//
//    public JobRecommendation(Job job, double score) {
//        this.job = job;
//        this.score = score;
//    }
//
//}

