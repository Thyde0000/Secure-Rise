let alarmIsActive = false;

//Get Alarms from Backend API
document.addEventListener('DOMContentLoaded', function() {
    //Select Alarm Container
    const alarmsContainer = document.querySelector('.alarms-container');
    const alarmForm = document.querySelector('.edit-alarm-form');
    // Fetch and add alarms to container
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    //GET All Alarms From BACKEND API
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
                    //If User Clicks Save
                    alarmForm.querySelector('.save').addEventListener('click', () => {
                        //Create Alarm Entity with Edited Data
                        const editedAlarm = {
                            alarmName: alarmForm.editedTitle.value,
                            startingTime: alarmForm.editedTime.value,
                            alarmSound: alarmForm.editedSound.value,
                            playingSound: alarm.playingSound,
                            enabled: alarmForm.editedEnabled.checked
                        };
                        //Call editAlarm Method with API Call Request, Alarm Entity, and Reload Page if Successful
                        editAlarm(alarm.id, editedAlarm, () => {
                            window.location.reload();
                        });
                    });
                    //If User Clicks Cancel
                    //Hide Form and Clear Form Data
                    alarmForm.querySelector('.cancel').addEventListener('click', () => {
                        alarmForm.style.display = 'none';
                        alarmForm.editedTitle.value = '';
                        alarmForm.editedTime.value = '';
                        alarmForm.editedSound.value = '';
                        alarmForm.editedEnabled.checked = false;
                });
            });
                //If User Clicks Delete
                //Call deleteAlarm Method with API Call Request, Alarm ID, and Reload Page if Successful
                deleteButton.addEventListener('click', () => {
                    deleteAlarm(alarm.id, () => {
                        window.location.reload();
                    });
                });

                //Append Each Alarm DIV to the Alarms Container That Was Fetched From the GET Request
                alarmsContainer.appendChild(alarmDiv);
            });
        }
    })
    //Catch Any Errors with API Call
    .catch(error => console.error('Error:', error));
});

//Add Alarms to DOM with addAlarmForm
document.addEventListener('DOMContentLoaded', function() {
    //Select Add Alarm Button
    const addAlarmButton = document.querySelector('.add-alarm-btn');
    //Select Add Alarm Form
    const addAlarmForm = document.querySelector('.edit-alarm-form');

    //When User Clicks Add Alarm Button, Show Add Alarm Form
    addAlarmButton.addEventListener('click', () => {
        addAlarmForm.style.display = 'flex';
    });
    //When User Clicks Cancel, Hide Add Alarm Form and Clear Form Data
    addAlarmForm.querySelector('.cancel').addEventListener('click', () => {
        addAlarmForm.style.display = 'none';
        addAlarmForm.newTitle.value = '';
        addAlarmForm.newTime.value = '';
        addAlarmForm.newSound.value = '';
        addAlarmForm.newEnabled.checked = false;
    });
    //When User Clicks Save
    addAlarmForm.querySelector('.save-edited-alarm').addEventListener('click', () => {
        //Create Alarm Entity with Form Data
        const newAlarm = {
            alarmName: addAlarmForm.editedTitle.value,
            startingTime: addAlarmForm.editedTime.value,
            alarmSound: addAlarmForm.editedSound.value,
            playingSound: false,
            enabled: addAlarmForm.editedEnabled.checked
        };
        //Call addAlarm Method with API Call Request, Alarm Entity, and Reload Page if Successful
        addAlarm(newAlarm, () => {
            window.location.reload();
        });
    });
});


//Edit Alarm with Backend API Call
function editAlarm(id, alarm, callback) {
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    headers.set('Content-Type', 'application/json');
    fetch(`http://192.168.0.178/api/alarm/${id}`, {
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


//Add Alarm with Backend API Call
function addAlarm(alarm, callback) {
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    headers.set('Content-Type', 'application/json');
    fetch('http://192.168.0.178/api/alarm', {
        method: 'POST',
        headers: headers,
        credentials: 'include',
        body: JSON.stringify(alarm)
    })
    .then(response => {
        if (response.status === 201) {
            // Alarm added successfully, reload the page
            if(callback && typeof callback === 'function') {
                callback();
            }
        }
        else{
            console.log('Error adding alarm:', response.status);
        }
    });
}


//Delete Alarm with Backend API Call
function deleteAlarm(id, callback) {
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    fetch(`http://192.168.0.178/api/alarm/${id}`, {
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

//Check Alarm Times Every 30 Seconds
function checkAlarmTimes(){
    //Get Current Time
    const currentTime = new Date();
    //Format Current Time to Match Alarm Time Format
    const currentTimeString = currentTime.toString();
    const timeMatch = currentTimeString.match(/\d{2}:\d{2}/);
    const formattedTime = timeMatch[0] + ':00';
    //Get Alarms from Backend API
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    fetch('http://192.168.0.178/api/alarm', {
        method: 'GET',
        headers: headers,
        credentials: 'include'
    })
    .then(res => res.json())
    .then(data => {
        if (data.length > 0) {
            data.forEach(alarm => {
                //Get Each Alarm Time From DB and Format to Match Current Time Format
                const alarmTime = alarm.startingTime;
                const alarmString = alarmTime.toString();
                //If Alarm Time Matches Current Time and Alarm is Enabled and Alarm is Not Playing Sound, Play Alarm
                if(formattedTime === alarmString && alarm.enabled === true && alarm.playingSound === false){
                    playAlarm(alarm);
                    alarmIsActive = true;

                }
                else if(formattedTime == alarmString && alarmIsActive == true){
                    addToQueue(alarm);
                }
                //If Alarm Time Matches Current Time and Alarm is Enabled and Alarm is Playing Sound, SHOW FORM (IN PROGRESS)
            });
        }
    });
}
//Call checkAlarmTimes Every 30 Seconds
setInterval(checkAlarmTimes, 30000);

//Generate Random String to Stop Alarm
function generateRandomString(countOfCharacters){
    const randomString = Math.random().toString(36).substring(2, countOfCharacters);
    return randomString;
}

function addToQueue(alarm){
    const headers = new Headers();
    headers.set('Authorization','Basic' + btoa('user:user'));
    fetch('http://192.168.0.178/api/alarm/queue/${alarm.id}',{
        method: 'PATCH',
        Headers: headers,
        credentials: 'include'
    })
    .then(response =>{
        if(response.status == 200){
            console.log('Queued Alarm');
        }
    })
}


//Play Alarm with Backend API Call
function playAlarm(alarm){
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    fetch(`http://192.168.0.178/api/alarm/play/${alarm.id}`, {
        method: 'PATCH',
        headers: headers,
        credentials: 'include'
    })
    .then(response => {
        if (response.status === 200) {
            let counter = 8;
            let newRandomString = '';
            // Alarm playing successfully
            const turnOffAlarmPrompt = document.querySelector('.turn-off-alarm-prompt');
            //Random String to Stop Alarm
            const turnOffAlarmStringElement = turnOffAlarmPrompt.querySelector('.turn-off-alarm-string');
            //Message to User
            const turnOffAlarmMessage = turnOffAlarmPrompt.querySelector('#prompt');
            //Set Random String to Stop Alarm
            const randomString = generateRandomString(counter);
            newRandomString = randomString;
            turnOffAlarmStringElement.textContent = randomString;
            //Disable Right Click on Random String
            turnOffAlarmStringElement.addEventListener('contextmenu', function (e) {
                e.preventDefault();
            });
            //Show Prompt
            turnOffAlarmPrompt.style.display = 'flex';
            //When User Clicks Turn Off Alarm Button
            turnOffAlarmPrompt.querySelector('.turn-off-alarm').addEventListener('click', () => {
                //If User Input Matches Random String, Stop Alarm
                if(turnOffAlarmPrompt.querySelector('.turn-off-alarm-input').value === newRandomString){
                    stopAlarm(alarm.id, () => {
                        window.location.reload();
                    });
                }
                //Else, Display Incorrect String Message & Generate New Random String
                else{
                    counter++;
                    console.log(counter);
                    turnOffAlarmMessage.textContent = 'Incorrect String. Try Again.';
                    newRandomString = generateRandomString(counter);
                    turnOffAlarmStringElement.textContent = newRandomString;
                    console.log(turnOffAlarmStringElement.textContent);
                    console.log(turnOffAlarmStringElement);
                }
            });
        }
        else{
            console.log('Error editing alarm:', response.status);
        }
    });
}

//Stop Alarm with Backend API Call
function stopAlarm(id, callback) {
    const headers = new Headers();
    headers.set('Authorization', 'Basic ' + btoa('user:user'));
    fetch(`http://192.168.0.178/api/alarm/stop/${id}`, {
        method: 'PATCH',
        headers: headers,
        credentials: 'include'
    })
    .then(response => {
        if (response.status === 200) {
            // Alarm stopped successfully, remove the alarm's div from the DOM
            const turnOffAlarmPrompt = document.querySelector('.turn-off-alarm-prompt');
            turnOffAlarmPrompt.style.display = 'none';
            // Call the callback function if it exists to reload the page
            if(callback && typeof callback === 'function') {
                callback();
            }
        }
    });
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

//Convert Time to Military Time from Standard Time(From Frontend to DB)
function convertToMilitaryTime(standardTime) {
    const [time, period] = standardTime.split(' ');
    const [hours, minutes] = time.split(':');
    let militaryHours = hours;
}