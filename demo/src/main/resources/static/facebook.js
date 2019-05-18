const storage = {
    oauth2Provider: 'Facebook',
    accessToken: null,
    headers: null
};
const initialMessage = document.querySelector('#initial-message');
const userInfo = document.querySelector('#user-info');
const userInfoImage = userInfo.querySelector('img');
const userInfoTitle = userInfo.querySelector('.card-title');
const userInfoParagraphs = userInfo.querySelectorAll('.card-text');
const userInfoId = userInfoParagraphs[0];
const userInfoGender = userInfoParagraphs[1];
const userInfoLink = userInfo.querySelector('a');
const hiddenElements = document.querySelectorAll('.hidden');
const saveItemForm = document.querySelector('form');
const itemDescriptionInput = saveItemForm.querySelector('input');
const retrieveButtons = document.querySelectorAll('.btn-group .btn');
const retrieveAllButton = retrieveButtons[0];
const retrieveVisibleButton = retrieveButtons[1];
const itemTableBody = document.querySelector('.table tbody');

saveItemForm.addEventListener('submit', event => {
    event.preventDefault();
    saveItem();
});

retrieveAllButton.addEventListener('click', retrieveAll);

retrieveVisibleButton.addEventListener('click', retrieveVisible);

function login() {
    const options = {
        scope: 'email,user_gender,user_link'
    };

    FB.login(response => {
        if (!response.authResponse) {
            return;
        }

        createHeader(response.authResponse.accessToken);
    }, options);
}

function createHeader(accessToken) {
    storage.accessToken = accessToken;
    storage.headers = new Headers({
        'X-OAuth2-Provider': storage.oauth2Provider,
        Authorization: `Bearer ${accessToken}`
    });
    showElements();
}

function getUserInfo() {
    const request = new Request('users/me', {
        method: 'GET',
        headers: storage.headers
    });
    return fetch(request)
        .then(response => response.json())
        .then(userInfo => {
            userInfoImage.src = userInfo.picture;
            const titleAnchor = document.createElement('a');
            titleAnchor.href= `mailto:${userInfo.email}`;
            titleAnchor.text = userInfo.name;
            userInfoTitle.appendChild(titleAnchor);
            userInfoId.textContent = `User Id: ${userInfo.id}`;
            if (userInfo.gender) {
                userInfoGender.textContent = `Gender: ${userInfo.gender}`;
            }
            else {
                userInfoGender.classList.add('hidden');
            }
            if (userInfo.link) {
                userInfoLink.href = userInfo.link;
            }
            else {
                userInfoLink.classList.add('hidden');
            }
        })
        .catch(error => console.error('UserInfo error', error));
}

function showElements() {
    initialMessage.classList.add('hidden');
    hiddenElements.forEach(hiddenElement => hiddenElement.classList.remove('hidden'));
    getUserInfo();
}

function saveItem() {
    const description = itemDescriptionInput.value;

    if (!description.trim()) {
        return
    }

    const item = {
        description: description
    };
    const clonedHeaderObject = Array.from(storage.headers.entries())
        .reduce((obj, array) => {
            obj[array[0]] = array[1];
            return obj;
        }, {});
    const headers = new Headers({
        ...clonedHeaderObject,
        'Content-Type': 'application/json'
    });
    const request = new Request('items', {
        method: 'POST',
        headers: headers,
        body: JSON.stringify(item)
    });
    return fetch(request)
        .catch(error => console.error('Item save error', error))
        .finally(() => itemDescriptionInput.value = '');
}

function retrieveAll() {
    retrieve(true);
}

function retrieveVisible() {
    retrieve(false);
}

function retrieve(all) {
    const request = new Request(`items/${all ? 'all' : 'visible'}`, {
        method: 'GET',
        headers: storage.headers
    });

    while (itemTableBody.firstChild) {
        itemTableBody.removeChild(itemTableBody.firstChild);
    }

    return fetch(request)
        .then(response => response.json())
        .then(items => {
            items.forEach(item => {
                const itemRow = document.createElement('tr');
                const itemCellId = document.createElement('td');
                itemCellId.textContent = item.id;
                itemRow.appendChild(itemCellId);
                const itemCellDescription = document.createElement('td');
                itemCellDescription.textContent = item.description;
                itemRow.appendChild(itemCellDescription);
                const itemCellEmail = document.createElement('td');
                itemCellEmail.textContent = item.email;
                itemRow.appendChild(itemCellEmail);
                itemTableBody.appendChild(itemRow);
            });
        })
        .catch(error => console.error('Retrieve error', error));
}

window.fbAsyncInit = function() {
    FB.init({
        appId: '216750812448511',
        autoLogAppEvents: true,
        xfbml: true,
        version: 'v3.0'
    });

    login();
};

(function(d, s, id){
    var js, fjs = d.getElementsByTagName(s)[0];
    if (d.getElementById(id)) {return;}
    js = d.createElement(s); js.id = id;
    js.src = "https://connect.facebook.net/en_US/sdk.js";
    fjs.parentNode.insertBefore(js, fjs);
}(document, 'script', 'facebook-jssdk'));
