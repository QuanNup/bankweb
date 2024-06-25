document.addEventListener("DOMContentLoaded", function() {
    const accountSelector = document.getElementById('accountSelector');
    const transactionTypeSelector = document.getElementById('transactionTypeSelector');
    const transactionsTable = document.getElementById('transactionsTable');
    const transactionsTableBody = transactionsTable.querySelector('tbody');

    // Fetch accounts from API
    async function fetchAccounts() {
        try {
            const jwtToken=sessionStorage.getItem('token')
            const response = await fetch('http://localhost:8031/account/get-accounts-by-email',{
                method:'POST',
                headers:{
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify({
                    'email': sessionStorage.getItem('email')
                })
            });
            if (!response.ok) {
                throw new Error('Failed to fetch accounts');
            }
            const accounts = await response.json();
            populateAccountSelector(accounts);
        } catch (error) {
            console.error('Error fetching accounts:', error);
        }
    }

    // Populate accountSelector with account numbers
    function populateAccountSelector(data) {
        accountSelector.innerHTML = '<option value="">-- Select Account --</option>';
        data.accounts.forEach(account => {
            const option = document.createElement('option');
            option.value = account.accountNumber;
            option.textContent = account.accountNumber;
            accountSelector.appendChild(option);
        });
    }
    function formatDate(localDateTime) {
        const date = new Date(localDateTime);
        const day = String(date.getDate()).padStart(2, '0');
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const year = date.getFullYear();
        return `${day}/${month}/${year}`;
    }
    // Fetch transactions from API based on account number
    async function fetchTransactions(accountNumber) {
        const jwtToken=sessionStorage.getItem('token')
        try {
            const response = await fetch(`http://localhost:8031/receipt/get-receipt-by-accountNumber`,{
                method:'POST',
                headers:{
                    'Authorization': `Bearer ${jwtToken}`,
                    'Content-Type': 'application/json'
                },
                body:JSON.stringify({
                    'accountNumber': accountNumber
                })
            });
            if (!response.ok) {
                throw new Error('Failed to fetch transactions');
            }
            const transactions = await response.json();
            populateTransactionsTable(transactions);
        } catch (error) {
            console.error('Error fetching transactions:', error);
        }
    }

    // Populate transactions table
    function populateTransactionsTable(transactions) {
        transactionsTableBody.innerHTML = '';

        const selectedTransactionType = transactionTypeSelector.value.toLowerCase();

        transactions.forEach(transaction => {
           if(transaction.receiptType==selectedTransactionType){
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${transaction.accountNumber}</td>
                <td>${transaction.amount}</td>
                <td>${formatDate(transaction.transactionDate)}</td>
                ${selectedTransactionType === 'transfer' ? `<td>${transaction.receiveAccountNumber}</td>` : ''}
            `;
            transactionsTableBody.appendChild(row);
           }
        });

        // Show or hide the "Receive Account Number" column based on transaction type
        document.querySelectorAll('.transfer-only').forEach(cell => {
            cell.style.display = selectedTransactionType === 'transfer' ? 'block' : 'none';
        });
    }

    // Event listener for transaction type change
    transactionTypeSelector.addEventListener('change', function() {
        const selectedAccountNumber = accountSelector.value;
        if (selectedAccountNumber) {
            fetchTransactions(selectedAccountNumber);
        }
    });

    // Event listener for account selector change
    accountSelector.addEventListener('change', function() {
        const selectedAccountNumber = accountSelector.value;
        if (selectedAccountNumber) {
            fetchTransactions(selectedAccountNumber);
        }
    });

    // Fetch accounts on page load
    fetchAccounts();
});
