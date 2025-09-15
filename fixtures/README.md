# Fixtures for Excel Import Testing

This folder contains sample .xlsx files for testing the Excel import functionality. The Postman collection references these files via the `fixturesPath` environment variable.

## Available Files

### Template Files (for reference)
- `subject_import_template.xlsx` - Empty template for subjects
- `teacher_import_template.xlsx` - Empty template for teachers
- `classrooms_import_template.xlsx` - Empty template for classrooms
- `student_import_template.xlsx` - Empty template for students

### Sample Files (for testing)
- `subjects_sample.xlsx` - Sample subjects data for import testing
- `teachers_sample.xlsx` - Sample teachers data for import testing
- `classrooms_sample.xlsx` - Sample classrooms data for import testing
- `students_sample.xlsx` - Sample students data for import testing

## Sample Data Guidelines

### Subjects
- **Code**: Unique subject codes (e.g., MAT101, PHY102)
- **Name**: Subject names (e.g., Mathematics, Physics)
- **Description**: Brief description of the subject
- **Credits**: Number of credits (1-4)
- **Category**: Subject category (Science, Language, Arts, etc.)

### Teachers
- **NIP**: Unique teacher identification number
- **Name**: Full teacher name
- **Email**: Unique email address
- **Phone**: Phone number
- **Subject**: Subject they teach (should match existing subject codes)

### Classrooms
- **Name**: Classroom name (e.g., X IPA 1, XI IPS 2)
- **Grade**: Grade level (10, 11, 12)
- **Major**: Major/stream (IPA, IPS, Bahasa)
- **Homeroom Teacher**: Teacher's NIP or Email

### Students
- **NIS**: Unique student identification number
- **Name**: Full student name
- **Email**: Unique email address
- **Phone**: Phone number
- **Classroom**: Classroom name (should match existing classroom names)

## Testing Tips

- For Classrooms: ensure `Major` matches existing majors and `Homeroom Teacher` matches an existing teacher's NIP or Email.
- For Teachers: use unique NIP and Email when not running with `dryRun=true`.
- For Students: follow the header order in the downloaded template.
- Use `dryRun=true` first to validate data before actual import.
- Use `mode=upsert` to update existing records instead of failing on duplicates.

## Quick Start

1. Pull fresh templates into this folder:
   - Windows PowerShell: `./pull_templates.ps1 -BaseUrl http://localhost:8080`
   - Or manually download via the APIs listed in README.

2. Populate sample files with test data following the guidelines above.

3. Import the Postman collection and set the `fixturesPath` environment variable to this folder.

4. Run the import requests with `dryRun=true` first to validate your data.
