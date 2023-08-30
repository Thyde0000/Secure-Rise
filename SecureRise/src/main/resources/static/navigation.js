document.addEventListener('DOMContentLoaded', function() {
    const wrapper = document.querySelector('.wrapper');

    // Fetch and display sidebar navigation
    fetch('wrapper.html')
    .then(res => res.text())
    .then(data => {
        wrapper.innerHTML = data;
    });
});
