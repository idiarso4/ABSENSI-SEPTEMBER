// Performance Analytics with Dynamic Data and Visualization
document.addEventListener('DOMContentLoaded', function() {
    loadAnalyticsData();
});

// Load all analytics data
function loadAnalyticsData() {
    const token = getToken();
    if (!token) {
        window.location.href = '/login';
        return;
    }

    // Load data from different endpoints
    Promise.all([
        fetch('/api/analytics/overview', {
            headers: {'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json'}
        }).then(res => res.json()),
        fetch('/api/analytics/top-performers', {
            headers: {'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json'}
        }).then(res => res.json()),
        fetch('/api/analytics/department-performance', {
            headers: {'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json'}
        }).then(res => res.json()),
        fetch('/api/analytics/performance-trends', {
            headers: {'Authorization': `Bearer ${token}`, 'Content-Type': 'application/json'}
        }).then(res => res.json())
    ]).then(([overview, topPerformers, departmentAnalysis, trends]) => {
        updateOverviewCards(overview);
        createPerformanceChart(overview);
        createTrendsChart(trends);
        updateTopPerformers(topPerformers);
        updateDepartmentAnalysis(departmentAnalysis);
    }).catch(error => {
        console.error('Error loading analytics data:', error);
        showError('Failed to load performance analytics data');
    });
}

// Update overview KPI cards
function updateOverviewCards(data) {
    document.getElementById('totalReviews').textContent = data.totalReviews || 0;
    document.getElementById('averageRating').textContent =
        data.averageRating ? data.averageRating.toFixed(1) : '0.0';

    // Count high performers from data
    const highPerformers = data.performanceDistribution ?
        (data.performanceDistribution['High Performer'] || 0) + (data.performanceDistribution['Excellent Performer'] || 0) : 0;
    document.getElementById('highPerformers').textContent = highPerformers;

    // Calculate completion rate based on approved/submitted reviews
    const totalReviews = data.totalReviews || 0;
    const completedReviews = (data.statusDistribution ?
        (data.statusDistribution['APPROVED'] || 0) + (data.statusDistribution['SUBMITTED'] || 0) : 0);
    const completionRate = totalReviews > 0 ? ((completedReviews / totalReviews) * 100).toFixed(1) : '0.0';
    document.getElementById('completionRate').textContent = `${completionRate}%`;
}

// Create performance distribution chart
function createPerformanceChart(data) {
    const ctx = document.getElementById('performanceChart');
    if (!ctx) return;

    const performanceData = data.performanceDistribution || {};
    const labels = Object.keys(performanceData);
    const values = Object.values(performanceData);

    new Chart(ctx.getContext('2d'), {
        type: 'doughnut',
        data: {
            labels: labels,
            datasets: [{
                data: values,
                backgroundColor: [
                    '#28a745', // High Performer - Green
                    '#007bff', // Good Performer - Blue
                    '#ffc107', // Average Performer - Yellow
                    '#fd7e14', // Low Performer - Orange
                    '#dc3545'  // Under Performer - Red
                ],
                borderWidth: 2
            }]
        },
        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: 'bottom'
                },
                title: {
                    display: true,
                    text: 'Performance Distribution by Category'
                }
            }
        }
    });
}

// Create performance trends chart
function createTrendsChart(data) {
    const ctx = document.getElementById('trendsChart');
    if (!ctx) return;

    new Chart(ctx.getContext('2d'), {
        type: 'line',
        data: {
            labels: data.years || [],
            datasets: [{
                label: 'Average Rating',
                data: data.averageRatings || [],
                borderColor: '#007bff',
                backgroundColor: 'rgba(0, 123, 255, 0.1)',
                borderWidth: 3,
                fill: true
            }, {
                label: 'Productivity Score',
                data: data.averageProductivity || [],
                borderColor: '#28a745',
                backgroundColor: 'rgba(40, 167, 69, 0.1)',
                borderWidth: 2,
                hidden: true
            }, {
                label: 'Quality Score',
                data: data.averageQuality || [],
                borderColor: '#ffc107',
                backgroundColor: 'rgba(255, 193, 7, 0.1)',
                borderWidth: 2,
                hidden: true
            }, {
                label: 'Attendance Score',
                data: data.averageAttendance || [],
                borderColor: '#6f42c1',
                backgroundColor: 'rgba(111, 66, 193, 0.1)',
                borderWidth: 2,
                hidden: true
            }, {
                label: 'Teamwork Score',
                data: data.averageTeamwork || [],
                borderColor: '#fd7e14',
                backgroundColor: 'rgba(253, 126, 20, 0.1)',
                borderWidth: 2,
                hidden: true
            }]
        },
        options: {
            responsive: true,
            plugins: {
                title: {
                    display: true,
                    text: 'Performance Trends Over Time'
                },
                legend: {
                    display: true,
                    position: 'top'
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    max: 100,
                    title: {
                        display: true,
                        text: 'Score (%)'
                    }
                },
                x: {
                    title: {
                        display: true,
                        text: 'Year'
                    }
                }
            }
        }
    });
}

// Update top performers list
function updateTopPerformers(performers) {
    const container = document.getElementById('topPerformersList');
    if (!container) return;

    if (!performers || performers.length === 0) {
        container.innerHTML = '<p class="text-muted">No performance data available</p>';
        return;
    }

    const html = performers.map((performer, index) => `
        <div class="d-flex justify-content-between align-items-center border-bottom py-2">
            <div class="d-flex align-items-center">
                <div class="bg-primary text-white rounded-circle d-flex align-items-center justify-content-center me-3"
                     style="width: 40px; height: 40px; font-weight: bold;">
                    ${index + 1}
                </div>
                <div>
                    <strong>${performer.employeeName}</strong>
                    <br>
                    <small class="text-muted">${performer.employeeIdNumber} - ${performer.department}</small>
                </div>
            </div>
            <div class="text-end">
                <span class="badge bg-${getPerformanceBadgeColor(performer.averageRating)}">
                    ${performer.averageRating?.toFixed(1) || '0.0'} ⭐
                </span>
                <br>
                <small class="text-muted">${performer.performanceLevel}</small>
            </div>
        </div>
    `).join('');

    container.innerHTML = html;
}

// Update department analysis
function updateDepartmentAnalysis(departments) {
    const container = document.getElementById('departmentAnalysisList');
    if (!container) return;

    if (!departments || departments.length === 0) {
        container.innerHTML = '<p class="text-muted">No department data available</p>';
        return;
    }

    const html = departments.map(dept => `
        <div class="d-flex justify-content-between align-items-center border-bottom py-2">
            <div>
                <strong>${dept.department}</strong>
                <br>
                <small class="text-muted">${dept.totalEmployees} employees, ${dept.reviewCount} reviews</small>
            </div>
            <div class="text-end">
                <span class="badge bg-${getPerformanceBadgeColor(dept.averageRating)}">
                    ${dept.averageRating?.toFixed(1) || '0.0'} ⭐
                </span>
                <br>
                <small class="text-muted">${dept.performanceLevel}</small>
            </div>
        </div>
    `).join('');

    container.innerHTML = html;
}

// Generate performance report
function generateReport() {
    const year = document.getElementById('reportYear').value;
    const token = getToken();

    fetch(`/api/analytics/performance-report?year=${year}`, {
        headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        displayReport(data);
    })
    .catch(error => {
        console.error('Error generating report:', error);
        showError('Failed to generate performance report');
    });
}

// Display generated report
function displayReport(data) {
    const reportDiv = document.getElementById('reportResults');
    const summaryDiv = document.getElementById('reportSummary');

    const summary = `
        <strong>Report Year:</strong> ${data.reportYear}<br>
        <strong>Generated:</strong> ${new Date(data.generatedAt).toLocaleDateString()}<br>
        <strong>Average Rating:</strong> ${data.averageRating?.toFixed(2) || 'N/A'}<br>
        <strong>Average Productivity:</strong> ${data.averageProductivity?.toFixed(2) || 'N/A'}%<br>
        <strong>Total Departments:</strong> ${data.departmentBreakdown?.length || 0}<br>
        <strong>Top Performers:</strong> ${data.topPerformers?.length || 0}
    `;

    if (data.topPerformers && data.topPerformers.length > 0) {
        summary += `<br><br><strong>Top Performers:</strong><br>`;
        data.topPerformers.forEach(performer => {
            summary += `• ${performer.employeeName} (${performer.averageRating?.toFixed(1) || '0.0'}⭐)<br>`;
        });
    }

    summaryDiv.innerHTML = summary;
    reportDiv.style.display = 'block';
}

// Export data functionality (placeholder)
function exportData() {
    alert('Data export functionality will be implemented in the next phase. This will allow downloading performance data as CSV/Excel files.');
}

// Helper functions
function getToken() {
    return localStorage.getItem('token') || '';
}

function getPerformanceBadgeColor(rating) {
    if (!rating) return 'secondary';
    if (rating >= 4.5) return 'success';
    if (rating >= 4.0) return 'primary';
    if (rating >= 3.5) return 'info';
    if (rating >= 3.0) return 'warning';
    return 'danger';
}

function showError(message) {
    const toast = document.createElement('div');
    toast.className = 'alert alert-danger alert-dismissible fade show position-fixed';
    toast.style.cssText = 'top: 100px; right: 10px; z-index: 9999; min-width: 300px;';
    toast.innerHTML = `
        <strong>Error!</strong> ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;

    document.body.appendChild(toast);

    setTimeout(() => {
        if (toast.parentNode) {
            toast.parentNode.removeChild(toast);
        }
    }, 5000);
}

// Auto-refresh data every 10 minutes
setInterval(loadAnalyticsData, 600000); // 10 minutes