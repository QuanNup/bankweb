function sendDataToSever(url,data){
    const axiosInstance = axios.create({
        baseURL: 'https://api.example.com', // Thay thế URL của API của bạn
        timeout: 5000 // Thời gian chờ tối đa cho mỗi yêu cầu (ms)
      });
    axiosInstance.post('http://localhost:8080/Login', user)
                .then(function (response) {
                    console.log(response.data);
                    alert('Data sent successfully!');
                })
                .catch(function (error) {
                    console.error(error);
                    alert('Failed to send data.');
                });
};