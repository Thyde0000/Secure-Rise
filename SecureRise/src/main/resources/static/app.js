document.addEventListener('DOMContentLoaded', function() {
    const nav = document.querySelector('.wrapper');
    fetch('wrapper.html')
    .then(res => res.text())
    .then(data => {
        nav.innerHTML = data;
    });
});
