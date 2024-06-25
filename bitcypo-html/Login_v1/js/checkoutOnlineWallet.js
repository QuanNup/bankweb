
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
document.addEventListener('DOMContentLoaded', () => {
    const accountSelector = document.getElementById('accountSelector');
    const accountsTableBody = document.querySelector('#accountsTable tbody');
    const cardAccountNumber = document.getElementById('cardAccountNumber');
    const cardAccountHolderName = document.getElementById('cardAccountHolderName');
    const cardAccountType = document.getElementById('cardAccountType');
    const cardAccountDate = document.getElementById('cardAccountDate');
    const cardBackground = document.querySelector('.card-background');

    // Replace 'your_jwt_token_here' with the actual JWT token
    const jwtToken = sessionStorage.getItem("token");
    const getAccountsURL = 'http://localhost:8031/account/get-accounts-by-email';
    const email = sessionStorage.getItem("email")
    // Fetch accounts from API
    fetch(getAccountsURL, {
        method: 'POST',
        headers: {
            'Authorization': `Bearer ${jwtToken}`,
            'Content-Type': 'application/json'
        },
        body:
            JSON.stringify({
                "email":email
            })
        
        
    })
    .then(response => response.json())
    .then(data => {
        // Populate account selector
        data.accounts.forEach(account => {
            const option = document.createElement('option');
            const userName = sessionStorage.getItem('userProfile');
            option.value = account.accountNumber;
            option.textContent = `${userName} - ${account.accountNumber}-${account.accountType}`;
            accountSelector.appendChild(option);
        });

        // Display account details in the table and card when an account is selected
        accountSelector.addEventListener('change', () => {
            const userName = sessionStorage.getItem('userProfile');
            const selectedAccountNumber = accountSelector.value;
            const selectedAccount = data.accounts.find(account => account.accountNumber === selectedAccountNumber);

            // Clear the table body
            accountsTableBody.innerHTML = '';

            if (selectedAccount) {
                const row = document.createElement('tr');
                row.innerHTML = `
                    <td>${selectedAccount.accountNumber}</td>
                    <td>${userName}</td>
                    <td>${selectedAccount.accountType}</td>
                    <td>${selectedAccount.balance}</td>

                `;
                accountsTableBody.appendChild(row);

                // Update card details
                cardAccountNumber.textContent = formatAccountNumber(selectedAccount.accountNumber);
                cardAccountHolderName.textContent = userName;
                cardAccountType.textContent = selectedAccount.accountType === 'checking' ? 'DEBIT CARD' : 'SAVING CARD';
                cardAccountDate.textContent = `Member Since: ${formatLocalDateTime(selectedAccount.createDate)}`;
                updateCardBackground(selectedAccount.accountType);
            } else {
                // Clear card details if no account is selected
                cardAccountNumber.textContent = 'ACC0001';
                cardAccountHolderName.textContent = 'Your Name';
                cardAccountType.textContent = 'Checking';
                cardAccountDate.textContent = 'Member Since:';
                cardBackground.style.backgroundColor = '#4CAF50';
            }
        });
    })
    .catch(error => console.error('Error fetching accounts:', error));

    function formatLocalDateTime(dateTimeString) {
        const parts = dateTimeString.split('T')[0].split('-'); // Tách phần ngày và bỏ qua phần giờ
        const year = parts[0];
        const month = parts[1];
        const day = parts[2];
        return `${day}/${month}/${year}`; // Định dạng thành dd/MM/yyyy
      }
    function formatAccountNumber(accountNumber) {
        const parts = [];
        for (let i = 0; i < accountNumber.length; i += 4) {
            parts.push(accountNumber.substr(i, 4));
        }
        return parts.join(' ');
    }

    function updateCardBackground(accountType) {
        cardBackground.style.backgroundColor = accountType === 'saving' ? '#f44336' : '#4CAF50';
    }
});