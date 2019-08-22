const storage = {
    oauth2Provider: 'Google',
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
const imageContainer = document.querySelector('#image-container');
const image = imageContainer.querySelector('img');
const toggleAccessTokenButton = imageContainer.querySelector('button');

saveItemForm.addEventListener('submit', event => {
    event.preventDefault();
    saveItem();
});

retrieveAllButton.addEventListener('click', retrieveAll);

retrieveVisibleButton.addEventListener('click', retrieveVisible);

toggleAccessTokenButton.addEventListener('click', toggleAccessToken);

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

function toggleAccessToken() {
    const url = new URL(image.src);
    const searchParams = url.searchParams;
    if (searchParams.get('access_token')) {
        image.src = url.pathname;
    }
    else {
        searchParams.set('oauth2_provider', storage.oauth2Provider);
        searchParams.set('access_token', storage.accessToken);
        image.src = url.pathname + url.search;
    }
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

function createHeader(accessToken) {
    storage.accessToken = accessToken;
    storage.headers = new Headers({
        'X-OAuth2-Provider': storage.oauth2Provider,
        Authorization: `Bearer ${accessToken}`
    });
    showElements();
}

function initClient() {
    return gapi.client.init({
        discoveryDocs: ["https://people.googleapis.com/$discovery/rest?version=v1"],
        clientId: '692293897979-f8l8e01f513evte972148bhc73jbnl2v.apps.googleusercontent.com',
        scope: 'openid email'
    }).then(() => {
        return gapi.auth2.getAuthInstance().signIn().then(response => {
            createHeader(response.Zi.access_token);
            toggleAccessTokenButton.disabled = false;
        });
    });
}

function handleClientLoad() {
    gapi.load('client:auth2', initClient);
}

const script = document.createElement('script');
script.async = '';
script.defer = '';
script.src = 'https://apis.google.com/js/api.js';
script.onload = handleClientLoad;
script.onreadystatechange = () => {
    if (this.readyState === 'complete') {
        this.onload();
    }
}
document.head.appendChild(script);
