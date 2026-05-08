// PurpleForce Gym - app.js

// Auto-ocultar alertas después de 5 segundos
document.addEventListener('DOMContentLoaded', function () {
    const alerts = document.querySelectorAll('.alert');
    alerts.forEach(function (alert) {
        setTimeout(function () {
            alert.style.transition = 'opacity 0.5s ease';
            alert.style.opacity = '0';
            setTimeout(function () { alert.remove(); }, 500);
        }, 5000);
    });

    // Marcar link activo en la navbar según la URL actual
    const currentPath = window.location.pathname;
    document.querySelectorAll('.nav-link').forEach(function (link) {
        if (link.getAttribute('href') && currentPath.startsWith(link.getAttribute('href'))
                && link.getAttribute('href') !== '/') {
            link.classList.add('active');
        }
    });
});
