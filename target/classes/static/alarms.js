//Get Alarms from Backend API
document.addEventListener('DOMContentLoaded', function() {
    //Select Alarm Container
    const alarmsContainer = document.querySelector('.alarms-container');
    const alarmForm = document.querySelector('.edit-alarm-form');
    // Fetch and add alarms to container
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    //GET Alarms From BACKEND API
    fetch('http://192.168.0.178:8080/api/alarm', {
        method: 'GET',
        headers: headers,
        credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {
        if (data.length > 0) {
            data.forEach(alarm => {
                //Create an Alarm DIV
                const alarmDiv = document.createElement('div');
                //Each Alarm Will Be Named Alarm
                alarmDiv.className = 'alarm';
                //Made Enabled Status Uppercase
                const enabledStatus = String(alarm.enabled).charAt(0).toUpperCase() + String(alarm.enabled).slice(1);
                //Convert Time to Standard Time from Military Time(From DB to Frontend)
                const time = convertToStandardTime(alarm.startingTime);
                //Each Alarm DIV Will Contain Alarm Name, Time, Enabled Status, Edit Button, and Delete Button
                alarmDiv.innerHTML = `
                    <div class="alarm-title">Alarm: ${alarm.alarmName}</div>
                    <div class="alarm-time">Time: ${time}</div>
                    <div class="alarm-enabled">Enabled: ${enabledStatus}</div>
                    <button class="edit-button">Edit</button>
                    <button class="delete-button">Delete</button>
                `;
                //Set Each Alarm's ID to it's respective ID in the DB
                alarmDiv.setAttribute('data-alarm-id', alarm.id);
                // Attach editButton and deleteButton event listeners
                const editButton = alarmDiv.querySelector('.edit-button');
                const deleteButton = alarmDiv.querySelector('.delete-button');
                //Call editAlarm Method with API Call Request, Alarm Entity, and Reload Page if Successful
                editButton.addEventListener('click', () => {
                    alarmForm.style.display = 'flex';
                    //Fill in the edit alarm form with the alarm's data
                    alarmForm.editedTitle.value = alarm.alarmName;
                    alarmForm.editedTime.value = alarm.startingTime;
                    alarmForm.editedSound.value = alarm.alarmSound;
                    alarmForm.editedEnabled.checked = alarm.enabled;
                    alarmForm.querySelector('.save-edited-alarm').addEventListener('click', () => {
                        const editedAlarm = {
                            alarmName: alarmForm.editedTitle.value,
                            startingTime: alarmForm.editedTime.value,
                            alarmSound: alarmForm.editedSound.value,
                            playingSound: alarm.playingSound,
                            enabled: alarmForm.editedEnabled.checked
                        };
                        console.log(editedAlarm);
                        editAlarm(alarm.id, editedAlarm, () => {
                            window.location.reload();
                        });
                    });

                    alarmForm.querySelector('.cancel').addEventListener('click', () => {
                        alarmForm.style.display = 'none';
                        alarmForm.editedTitle.value = '';
                        alarmForm.editedTime.value = '';
                        alarmForm.editedSound.value = '';
                        alarmForm.editedEnabled.checked = false;
                });
            });
                //Call deleteAlarm Method with API Call Request, If Successful, Reload Page
                deleteButton.addEventListener('click', () => {
                    deleteAlarm(alarm.id, () => {
                        window.location.reload();
                    });
                });

                //Add Alarm To Container
                alarmsContainer.appendChild(alarmDiv);
            });
        }
    })
    //Catch Any Errors with API Call
    .catch(error => console.error('Error:', error));
});

//Edit Alarm with Backend API Call
function editAlarm(id, alarm, callback) {
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    headers.set('Content-Type', 'application/json');
    fetch(`http://192.168.0.178:8080/api/alarm/${id}`, {
        method: 'PUT',
        headers: headers,
        credentials: 'include',
        body: JSON.stringify(alarm)
    })
    .then(response => {
        if (response.status === 200) {
            // Alarm edited successfully, reload the page
            if(callback && typeof callback === 'function') {
                callback();
            }
        }
        else{
            console.log('Error editing alarm:', response.status);
        }
    })
    .catch(error => console.error('Error editing alarm:', error));
}



//Delete Alarm with Backend API Call
function deleteAlarm(id, callback) {
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    fetch(`http://192.168.0.178:8080/api/alarm/${id}`, {
        method: 'DELETE',
        headers: headers,
        credentials: 'include'
    })
    //If Successful, Remove Alarm from DOM
    .then(response => {
        if (response.status === 200) {
            // Alarm deleted successfully, remove the alarm's div from the DOM
            const alarmDiv = document.querySelector(`[data-alarm-id="${id}"]`);
            if (alarmDiv) {
                alarmDiv.remove();
            }
            // Call the callback function if it exists to reload the page
            if(callback && typeof callback === 'function') {
                callback();
            }
        }
    })
    .catch(error => console.error('Error deleting alarm:', error));
}

//Convert Time to Standard Time from Military Time(From DB to Frontend)
function convertToStandardTime(militaryTime) {
    const [hours, minutes] = militaryTime.split(':');
    let period = 'AM';

    if (Number(hours) >= 12) {
        period = 'PM';
    }

    const standardHours = (Number(hours) % 12) || 12;

    return `${standardHours}:${minutes} ${period}`;
}