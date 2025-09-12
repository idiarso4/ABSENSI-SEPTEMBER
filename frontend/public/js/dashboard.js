// Dashboard with Dynamic KPIs and Chart Visualizations
document.addEventListener('DOMContentLoaded', function() {
    initializeResponsiveDashboard();
    setupMobileRefresh();
    addTouchGestures();
    loadKPIs();
    loadAttendanceChart();
    loadPayrollChart();
    loadRecentActivities();
    loadUpcomingEvents();
});

// Initialize responsive dashboard features
function initializeResponsiveDashboard() {
    // Handle window resize for charts
    let resizeTimeout;
    window.addEventListener('resize', function() {
        clearTimeout(resizeTimeout);
        resizeTimeout = setTimeout(function() {
            // Re-render charts on resize
            const attendanceChart = Chart.getChart('attendanceChart');
            const payrollChart = Chart.getChart('payrollChart');

            if (attendanceChart) attendanceChart.resize();
            if (payrollChart) payrollChart.resize();
        }, 250);
    });

    // Add mobile-friendly touch handling
    if ('ontouchstart' in window) {
        document.addEventListener('touchstart', function() {}, { passive: true });
    }
}

// Load KPI metrics from the backend
function loadKPIs() {
    fetch('/api/v1/dashboard/kpis', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`HTTP ${response.status}: ${response.statusText}`);
        }
        return response.json();
    })
    .then(data => {
        // Update static numbers with dynamic data
        const totalEmployeesEl = document.getElementById('totalEmployees');
        const presentTodayEl = document.getElementById('presentToday');
        const pendingLeavesEl = document.getElementById('pendingLeaves');
        const pendingPayrollEl = document.getElementById('pendingPayroll');

        if (totalEmployeesEl) totalEmployeesEl.textContent = data.totalEmployees || '120';
        if (presentTodayEl) presentTodayEl.textContent = data.presentToday || '105';
        if (pendingLeavesEl) pendingLeavesEl.textContent = data.pendingLeaves || '8';
        if (pendingPayrollEl) pendingPayrollEl.textContent = data.pendingPayroll || '15';
    })
    .catch(error => {
        console.error('Error loading KPIs:', error);
        // Keep static values as fallback
    });
}

// Load and create attendance trend chart
function loadAttendanceChart() {
    fetch('/api/v1/dashboard/attendance-trend', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(data => {
        createAttendanceChart(data);
    })
    .catch(error => {
        console.error('Error loading attendance data:', error);
        // Load with default data
        createAttendanceChart({
            labels: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'],
            present: [95, 115, 105, 120, 100, 80, 45],
            absent: [15, 25, 20, 10, 25, 15, 10]
        });
    });
}

// Load and create payroll monthly chart
function loadPayrollChart() {
    fetch('/api/v1/dashboard/payroll-monthly', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(async response => {
        // Handle non-2xx responses gracefully to avoid passing error payloads
        const data = await response.json().catch(() => ({}));
        if (!response.ok) {
            throw new Error(`${response.status} ${response.statusText}: ${data.message || data.error || 'Failed to fetch payroll data'}`);
        }
        return data;
    })
    .then(data => {
        createPayrollChart(data);
    })
    .catch(error => {
        console.error('Error loading payroll data:', error);
        // Load with default data
        createPayrollChart({
            months: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'],
            amounts: [125000, 118500, 132000, 145000, 138000, 152000]
        });
    });
}

// Create attendance trend chart using Chart.js
function createAttendanceChart(data) {
    const ctx = document.getElementById('attendanceChart');
    if (!ctx) return;

    new Chart(ctx.getContext('2d'), {
        type: 'line',
        data: {
            labels: data.labels,
            datasets: [{
                label: 'Present',
                data: data.present,
                borderColor: 'rgba(75, 192, 192, 1)',
                backgroundColor: 'rgba(75, 192, 192, 0.2)',
                borderWidth: 2,
                fill: true
            }, {
                label: 'Absent',
                data: data.absent,
                borderColor: 'rgba(255, 99, 132, 1)',
                backgroundColor: 'rgba(255, 99, 132, 0.2)',
                borderWidth: 2,
                fill: true
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: window.innerWidth < 768 ? 'Weekly Attendance' : 'Weekly Attendance Trend',
                    font: {
                        size: window.innerWidth < 768 ? 14 : 16
                    }
                },
                legend: {
                    display: true,
                    position: window.innerWidth < 768 ? 'bottom' : 'top',
                    labels: {
                        boxWidth: window.innerWidth < 768 ? 12 : 40,
                        font: {
                            size: window.innerWidth < 768 ? 10 : 12
                        }
                    }
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: window.innerWidth < 768 ? false : true,
                        text: 'Number of Employees'
                    },
                    ticks: {
                        font: {
                            size: window.innerWidth < 768 ? 10 : 12
                        }
                    }
                },
                x: {
                    title: {
                        display: window.innerWidth < 768 ? false : true,
                        text: 'Day of Week'
                    },
                    ticks: {
                        font: {
                            size: window.innerWidth < 768 ? 10 : 12
                        }
                    }
                }
            },
            layout: {
                padding: window.innerWidth < 768 ? {
                    top: 10,
                    bottom: 10,
                    left: 10,
                    right: 10
                } : {}
            }
        }
    });
}

// Create payroll monthly chart
function createPayrollChart(data) {
    const ctx = document.getElementById('payrollChart');
    if (!ctx) return;

    // Format amounts to currency - handle both double and BigDecimal formats
    const months = Array.isArray(data?.months) && data.months.length
        ? data.months
        : ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun'];

    let amounts;
    if (Array.isArray(data?.amounts) && data.amounts.length) {
        // Convert all amounts to numbers, handling both double and BigDecimal formats
        amounts = data.amounts.map(amount => Number(amount) || 0);
    } else {
        amounts = [125000, 118500, 132000, 145000, 138000, 152000];
    }

    // Convert to thousands for better display, ensure all values are numbers
    const formattedAmounts = (amounts || []).map(amount => {
        const num = Number(amount) || 0;
        return num / 1000;
    });

    new Chart(ctx.getContext('2d'), {
        type: 'bar',
        data: {
            labels: months,
            datasets: [{
                label: 'Monthly Payroll (in thousands)',
                data: formattedAmounts,
                backgroundColor: 'rgba(54, 162, 235, 0.8)',
                borderColor: 'rgba(54, 162, 235, 1)',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                title: {
                    display: true,
                    text: window.innerWidth < 768 ? 'Monthly Payroll' : 'Monthly Payroll Distribution',
                    font: {
                        size: window.innerWidth < 768 ? 14 : 16
                    }
                },
                legend: {
                    display: false
                }
            },
            scales: {
                y: {
                    beginAtZero: true,
                    title: {
                        display: window.innerWidth < 768 ? false : true,
                        text: 'Amount ($ thousands)'
                    },
                    ticks: {
                        font: {
                            size: window.innerWidth < 768 ? 10 : 12
                        }
                    }
                },
                x: {
                    title: {
                        display: window.innerWidth < 768 ? false : true,
                        text: 'Month'
                    },
                    ticks: {
                        font: {
                            size: window.innerWidth < 768 ? 10 : 12
                        }
                    }
                }
            },
            layout: {
                padding: window.innerWidth < 768 ? {
                    top: 10,
                    bottom: 10,
                    left: 10,
                    right: 10
                } : {}
            }
        }
    });
}

// Load recent activities dynamically
function loadRecentActivities() {
    fetch('/api/v1/dashboard/recent-activities', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(activities => {
        const container = document.getElementById('activitiesList');
        if (!container) return;

        // Ensure activities is an array
        const activitiesArray = Array.isArray(activities) ? activities : [];

        if (activitiesArray.length === 0) {
            container.innerHTML = '<li class="list-group-item text-center text-muted">No recent activities</li>';
            return;
        }

        const html = activitiesArray.map(activity => `
            <li class="list-group-item">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <strong>${activity.description || 'Activity'}</strong>
                        <br>
                        <small class="text-muted">${formatDateTime(activity.timestamp)}</small>
                    </div>
                    <span class="badge bg-secondary">${activity.type || 'General'}</span>
                </div>
            </li>
        `).join('');

        container.innerHTML = html;
    })
    .catch(error => {
        console.error('Error loading activities:', error);
    });
}

// Load upcoming events dynamically
function loadUpcomingEvents() {
    fetch('/api/v1/dashboard/upcoming-events', {
        headers: {
            'Authorization': `Bearer ${getToken()}`,
            'Content-Type': 'application/json'
        }
    })
    .then(response => response.json())
    .then(events => {
        const container = document.getElementById('upcomingEventsList');
        if (!container) return;

        // Ensure events is an array
        const eventsArray = Array.isArray(events) ? events : [];

        if (eventsArray.length === 0) {
            container.innerHTML = '<li class="list-group-item text-center text-muted">No upcoming events</li>';
            return;
        }

        const html = eventsArray.map(event => `
            <li class="list-group-item">
                <div class="d-flex justify-content-between align-items-start">
                    <div>
                        <strong>${event.title || 'Event'}</strong>
                        <br>
                        <small class="text-muted">${formatDateTime(event.dateTime)}</small>
                    </div>
                    <span class="badge bg-info">${event.type || 'General'}</span>
                </div>
            </li>
        `).join('');

        container.innerHTML = html;
    })
    .catch(error => {
        console.error('Error loading events:', error);
    });
}

// Helper function to get JWT token from localStorage
function getToken() {
    return localStorage.getItem('token') || '';
}

// Helper function to format date and time
function formatDateTime(dateTimeStr) {
    try {
        const date = new Date(dateTimeStr);
        return date.toLocaleString('en-US', {
            month: 'short',
            day: 'numeric',
            hour: 'numeric',
            minute: '2-digit',
            hour12: true
        });
    } catch (e) {
        return dateTimeStr;
    }
}

// Refresh dashboard data every 5 minutes
setInterval(function() {
    loadKPIs();
    loadAttendanceChart();
    loadPayrollChart();
}, 300000); // 5 minutes in milliseconds

// Mobile-optimized refresh button
function setupMobileRefresh() {
    // Create a floating refresh button for mobile
    if (window.innerWidth < 768) {
        const refreshBtn = document.createElement('button');
        refreshBtn.innerHTML = '🔄';
        refreshBtn.className = 'btn btn-primary position-fixed';
        refreshBtn.style.cssText = `
            bottom: 20px;
            right: 20px;
            border-radius: 50%;
            width: 50px;
            height: 50px;
            z-index: 1000;
            box-shadow: 0 4px 8px rgba(0,0,0,0.3);
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 1.2rem;
        `;

        refreshBtn.onclick = function() {
            // Add loading state
            const originalHTML = refreshBtn.innerHTML;
            refreshBtn.innerHTML = '<div class="loading-spinner" style="width: 20px; height: 20px; border-width: 2px;"></div>';
            refreshBtn.disabled = true;

            // Refresh all data
            Promise.all([
                loadKPIs(),
                loadAttendanceChart(),
                loadPayrollChart(),
                loadRecentActivities(),
                loadUpcomingEvents()
            ]).finally(() => {
                refreshBtn.innerHTML = originalHTML;
                refreshBtn.disabled = false;
            });
        };

        document.body.appendChild(refreshBtn);
    }
}

// Add swipe support for mobile
function addTouchGestures() {
    if ('ontouchstart' in window) {
        let startX, startY;

        document.addEventListener('touchstart', (e) => {
            startX = e.touches[0].clientX;
            startY = e.touches[0].clientY;
        });

        document.addEventListener('touchend', (e) => {
            if (!startX || !startY) return;

            const endX = e.changedTouches[0].clientX;
            const endY = e.changedTouches[0].clientY;
            const diffX = startX - endX;
            const diffY = startY - endY;

            // Detect swipe down to refresh
            if (Math.abs(diffX) < 100 && diffY < -100 && window.scrollY < 50) {
                loadKPIs();
                loadAttendanceChart();
                loadPayrollChart();
            }

            startX = startY = null;
        });
    }
}

