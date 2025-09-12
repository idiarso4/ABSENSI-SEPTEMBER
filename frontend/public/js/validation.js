// Validation utilities for the frontend

function validateEmail(email) {
    const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return re.test(email);
}

function validateRequired(value) {
    return value && value.trim().length > 0;
}

function validatePassword(password) {
    // At least 8 characters, one uppercase, one lowercase, one number
    const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d@$!%*?&]{8,}$/;
    return re.test(password);
}

function validatePhone(phone) {
    // Simple phone validation (adjust as needed for your requirements)
    const re = /^[\+]?[1-9][\d]{0,15}$/;
    return re.test(phone);
}

function validateDate(date) {
    return !isNaN(Date.parse(date));
}

// Form validation function
function validateForm(formId, rules) {
    const form = document.getElementById(formId);
    const errors = {};
    
    for (const [field, validations] of Object.entries(rules)) {
        const element = form.querySelector(`[name="${field}"]`) || document.getElementById(field);
        if (!element) continue;
        
        const value = element.type === 'checkbox' ? element.checked : element.value;
        
        for (const validation of validations) {
            if (validation.rule === 'required' && !validateRequired(value)) {
                errors[field] = validation.message || `${field} is required`;
                break;
            }
            
            if (validation.rule === 'email' && !validateEmail(value)) {
                errors[field] = validation.message || 'Please enter a valid email';
                break;
            }
            
            if (validation.rule === 'password' && !validatePassword(value)) {
                errors[field] = validation.message || 'Password must be at least 8 characters with uppercase, lowercase and number';
                break;
            }
            
            if (validation.rule === 'phone' && !validatePhone(value)) {
                errors[field] = validation.message || 'Please enter a valid phone number';
                break;
            }
            
            if (validation.rule === 'date' && !validateDate(value)) {
                errors[field] = validation.message || 'Please enter a valid date';
                break;
            }
            
            if (validation.rule === 'minLength' && value.length < validation.value) {
                errors[field] = validation.message || `${field} must be at least ${validation.value} characters`;
                break;
            }
        }
    }
    
    return errors;
}

// Display errors in the UI
function displayErrors(errors) {
    // Clear previous errors
    document.querySelectorAll('.error-message').forEach(el => el.remove());
    document.querySelectorAll('.is-invalid').forEach(el => el.classList.remove('is-invalid'));
    
    // Display new errors
    for (const [field, message] of Object.entries(errors)) {
        const element = document.querySelector(`[name="${field}"]`) || document.getElementById(field);
        if (element) {
            element.classList.add('is-invalid');
            
            // Create error message element
            const errorElement = document.createElement('div');
            errorElement.className = 'error-message text-danger small mt-1';
            errorElement.textContent = message;
            
            // Insert error message after the input
            element.parentNode.insertBefore(errorElement, element.nextSibling);
        }
    }
}

// Export functions for use in other scripts
window.Validation = {
    validateEmail,
    validateRequired,
    validatePassword,
    validatePhone,
    validateDate,
    validateForm,
    displayErrors
};