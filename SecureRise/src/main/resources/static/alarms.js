//Get Alarms OR Show Default Message
document.addEventListener('DOMContentLoaded', function() {
    const alarmsContainer = document.querySelector('.alarms-container'); // Reference the alarms container
    const defaultDiv = document.querySelector('.default'); // Reference the default div

    // Fetch and display alarm data
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));

    fetch('http://localhost:8080/api/alarm', {
        method: 'GET',
        headers: headers,
        credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {
        if (data.length > 0) {
            // Alarms are available, hide the default div and display alarms
            defaultDiv.style.display = 'none';
            data.forEach(alarm => {
                const alarmDiv = document.createElement('div');
                alarmDiv.className = 'alarm';

                const enabledStatus = String(alarm.enabled).charAt(0).toUpperCase() + String(alarm.enabled).slice(1);

                alarmDiv.innerHTML = `
                    <div class="alarm-title">Alarm: ${alarm.alarmName}</div>
                    <div class="alarm-time">Time: ${alarm.startingTime}</div>
                    <div class="alarm-enabled">Enabled: ${enabledStatus}</div>
                    <button class="edit-button">Edit</button>
                    <button class="delete-button">Delete</button>
                `;
                alarmDiv.setAttribute('data-alarm-id', alarm.id);
                // Attach event listeners to the buttons
                const editButton = alarmDiv.querySelector('.edit-button');
                const deleteButton = alarmDiv.querySelector('.delete-button');

                editButton.addEventListener('click', () => {
                    // Handle edit action
                });

                deleteButton.addEventListener('click', () => {
                    console.log('Delete button clicked for alarm ID:', alarm.id);
                    deleteAlarm(alarm.id);
                    window.location.reload();
                });

                alarmsContainer.appendChild(alarmDiv);
            });
        } else {
            // No alarms available, hide the alarms container and display default div
            alarmsContainer.style.display = 'none';
        }

    })
    .catch(error => console.error('Error:', error));
});


//Edit Alarm
function editAlarm(id) {
    
}


//Delete Alarm with Backend API Call
function deleteAlarm(id) {
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    fetch(`http://localhost:8080/api/alarm/${id}`, {
        method: 'DELETE',
        headers: headers,
        credentials: 'include'
    })
    .then(response => {
        if (response.status === 200) {
            // Alarm deleted successfully, remove the alarm's div from the DOM
            const alarmDiv = document.querySelector(`[data-alarm-id="${id}"]`);
            if (alarmDiv) {
                alarmDiv.remove();
            }
        }
    })
    .catch(error => console.error('Error deleting alarm:', error));
}