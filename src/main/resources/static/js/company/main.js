import { countries } from './utils/countries.js';
import { validateCompanyInfo, validateContact, validateAddress } from './utils/validation.js';

let formData = {
    companyInfo: {},
    contact: {},
    address: {}
};

// Update progress bar based on current step
function updateProgress(step) {
    const progress = ((step - 1) / 2) * 100;
    document.querySelector('.progress-bar').style.width = `${progress}%`;
}

// Show the specified step and update navigation
function showStep(step) {
    // Hide all forms
    document.querySelectorAll('.step-form').forEach(form => {
        form.classList.remove('active');
    });
    
    // Show the current form
    document.getElementById(`step${step}`).classList.add('active');
    
    // Update nav tabs
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    document.querySelector(`[data-step="${step}"]`).classList.add('active');
    
    // Update progress
    updateProgress(step);
}

// Navigate to next step
function nextStep(currentStep) {
    const form = document.getElementById(`step${currentStep}`);
    const formFields = new FormData(form);
    const data = Object.fromEntries(formFields);
    
    let errors = {};
    switch(currentStep) {
        case 1:
            errors = validateCompanyInfo(data);
            if (Object.keys(errors).length === 0) {
                formData.companyInfo = data;
                showStep(currentStep + 1);
            }
            break;
        case 2:
            errors = validateContact(data);
            if (Object.keys(errors).length === 0) {
                formData.contact = data;
                showStep(currentStep + 1);
            }
            break;
    }
    
    // Show errors if any
    displayErrors(errors);
}

// Navigate to previous step
function previousStep(currentStep) {
    if (currentStep > 1) {
        showStep(currentStep - 1);
    }
}

// Display form validation errors
function displayErrors(errors) {
    // Clear existing error messages
    document.querySelectorAll('.error-message').forEach(el => el.remove());
    
    // Display new error messages
    Object.entries(errors).forEach(([field, message]) => {
        const input = document.querySelector(`[name="${field}"]`);
        if (input) {
            const errorDiv = document.createElement('div');
            errorDiv.className = 'error-message text-danger mt-1';
            errorDiv.textContent = message;
            input.parentNode.appendChild(errorDiv);
        }
    });
}

// Submit the final form
function submitForm() {
    const addressForm = document.getElementById('step3');
    const addressData = Object.fromEntries(new FormData(addressForm));
    
    const errors = validateAddress(addressData);
    if (Object.keys(errors).length === 0) {
        formData.address = addressData;
        console.log('Form submitted:', formData);
        // Here you would typically send the data to your backend
        alert('Setup completed successfully!');
    } else {
        displayErrors(errors);
    }
}

// Initialize form
document.addEventListener('DOMContentLoaded', () => {
    showStep(1);
    
    // Populate country selectors
    const phoneCodeSelect = document.querySelector('select[name="phoneCode"]');
    const countrySelect = document.querySelector('select[name="countryCode"]');
    
    countries.forEach(country => {
        // Add phone code option
        const phoneOption = document.createElement('option');
        phoneOption.value = country.phone;
        phoneOption.textContent = `${country.code} ${country.phone}`;
        phoneCodeSelect.appendChild(phoneOption);
        
        // Add country option
        const countryOption = document.createElement('option');
        countryOption.value = country.code;
        countryOption.textContent = country.name;
        countrySelect.appendChild(countryOption);
    });
    
    // Add click handlers for nav tabs
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const step = parseInt(link.getAttribute('data-step'));
            showStep(step);
        });
    });
    
    // Make functions globally available
    window.nextStep = nextStep;
    window.previousStep = previousStep;
    window.submitForm = submitForm;
});