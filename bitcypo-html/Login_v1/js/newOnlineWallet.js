document.addEventListener('DOMContentLoaded', () => {
    const walletForm = document.getElementById('walletForm');
    const accountsTableBody = document.querySelector('#accountsTable tbody');
    const accountNumberInput = document.getElementById('accountNumber');
    const accountHolderNameInput = document.getElementById('accountHolderName');
    const accountTypeSelect = document.getElementById('accountType');
    const generateAccountNumberBtn = document.getElementById('generateAccountNumber');
    const cardAccountNumber = document.getElementById('cardAccountNumber');
    const cardAccountHolderName = document.getElementById('cardAccountHolderName');
    const cardAccountType = document.getElementById('cardAccountType');
    const cardBalance = document.getElementById('cardBalance');
    let accountCounter = 1;

    // Replace 'your_jwt_token_here' with the actual JWT token
    const jwtToken = sessionStorage.getItem("token");

    generateAccountNumberBtn.addEventListener('click', () => {
        const getAccountNumberURL = 'http://localhost:8031/account/get-account-number';
        fetch(getAccountNumberURL, {
            method: 'GET',
            mode: 'cors',
            headers: {
                'Authorization': `Bearer ${jwtToken}`
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                accountNumberInput.value = data;
                cardAccountNumber.textContent = data;
                
            })
            .catch(error => {
                console.error('Error fetching account number:', error);
                alert('Failed to fetch account number. Please try again later.');
            });
       
    });

    walletForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const registryAccountURL = 'http://localhost:8031/account/registry';
        
        const accountNumber = accountNumberInput.value;
        const accountHolderName = accountHolderNameInput.value;
        const accountType = accountTypeSelect.value;
        const initialBalance = 50; // Fixed initial balance
        fetch(registryAccountURL, {
            method: 'POST',
            mode: 'cors',
            headers: {
                'Authorization': `Bearer ${jwtToken}`,
                	'Content-Type': 'application/json'
            },
            body: JSON.stringify({
					accountNumber: accountNumber,
					accountType: accountType,
                    userEmail: sessionStorage.getItem("email")
				})
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(data => {
                alert("thêm tài khoản thành công")
            })
            .catch(error => {
                console.error('Error fetching account number:', error);
                alert('Failed to fetch account number. Please try again later.');
            });
        walletForm.reset();
                cardAccountHolderName.textContent = 'Your Name';
                cardAccountType.textContent = 'Checking';
        const account = {
            accountNumber,
            accountHolderName,
            accountType,
            balance: initialBalance.toFixed(2)
        };
        
        
        
        
    });
    function updateCard(account) {
        cardAccountNumber.textContent = account.accountNumber;
        cardBalance.textContent = account.balance;
        cardAccountHolderName.textContent = account.accountHolderName;
        cardAccountType.textContent = account.accountType;
    }

    function formatAccountNumber(accountNumber) {
        const parts = [];
        for (let i = 0; i < accountNumber.length; i += 4) {
            parts.push(accountNumber.substr(i, 4));
        }
        return parts.join(' ');
    }

    function setCurrentDate() {
        const currentDate = new Date();
        const formattedDate = formatDate(currentDate);
        const cardAccountDateElement = document.getElementById('cardAccountDate');
        cardAccountDateElement.textContent = 'Member Since: ' + formattedDate;
    }

    function formatDate(date) {
        let day = date.getDate();
        let month = date.getMonth() + 1;
        let year = date.getFullYear();

        // Add leading zero if day or month is a single digit
        if (day < 10) {
            day = '0' + day;
        }
        if (month < 10) {
            month = '0' + month;
        }

        return day + '/' + month + '/' + year;
    }

    setCurrentDate();
});

// Function to update card details based on form inputs
document.addEventListener('DOMContentLoaded', function() {
    const accountNumberInput = document.getElementById('accountNumber');
    const accountHolderNameInput = document.getElementById('accountHolderName');
    const accountTypeSelect = document.getElementById('accountType');
    const cardAccountNumber = document.getElementById('cardAccountNumber');
    const cardAccountHolderName = document.getElementById('cardAccountHolderName');
    const cardAccountType = document.getElementById('cardAccountType');
    
    accountNumberInput.addEventListener('input', updateCardDetails);
    accountHolderNameInput.addEventListener('input', updateCardDetails);
    accountTypeSelect.addEventListener('change', updateCardDetails);
    
    updateCardDetails(); // Initial update of card details
    
    function updateCardDetails() {
        let accountNumberValue = accountNumberInput.value.trim().replace(/\s/g, '');
        let accountHolderName = accountHolderNameInput.value.trim().toUpperCase();
        
        if (accountNumberValue === '') {
            accountNumberValue = 'XXXXXXXXXXXXXXXX';
        }
        if (accountHolderName === '') {
            const userName = sessionStorage.getItem("userProfile");
            accountHolderNameInput.value=userName
            accountHolderName = userName
        }
        
        const formattedAccountNumber = formatAccountNumber(accountNumberValue);
        
        cardAccountNumber.textContent = formattedAccountNumber;
        cardAccountHolderName.textContent = accountHolderName;
        cardAccountType.textContent = `${accountTypeSelect.value === 'Checking' ? 'DEBIT CARD' : 'SAVING CARD'}`;
        
        updateCardBackground(accountTypeSelect.value);
    }
    
    function updateCardBackground(accountType) {
        const cardBackground = document.querySelector('.card-background');
        cardBackground.style.backgroundColor = accountType === 'Saving' ? '#f44336' : '#4CAF50';
    }
    
    function formatAccountNumber(accountNumber) {
        const parts = [];
        for (let i = 0; i < accountNumber.length; i += 4) {
            parts.push(accountNumber.substr(i, 4));
        }
        return parts.join(' ');
    }
});
