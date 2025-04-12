// Guard to prevent multiple executions
if (!window.adminTaiKhoanInitialized) {
    window.adminTaiKhoanInitialized = true;

    document.addEventListener('DOMContentLoaded', function () {
        var isSubmitting = false; // Biến theo dõi trạng thái gửi
        console.log('DOMContentLoaded triggered for admin-taikhoan.js');
        const accountForm = document.getElementById("account-form");
        const submitButton = accountForm ? accountForm.querySelector('button[type="submit"]') : null;

        // Hàm xử lý form thêm tài khoản
        function addTaiKhoan(event) {
            event.preventDefault();
            if (isSubmitting || !submitButton) return;

            submitButton.disabled = true;
            const formData = new FormData(accountForm);
            const data = Object.fromEntries(formData);

            isSubmitting = true;
            fetch("/admin/addTaiKhoan", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(err => { throw new Error(err.message); });
                    }
                    return response.json();
                })
                .then(data => {
                    const message = data.message || "Tạo tài khoản thành công!";
                    showAlert(message.includes("thành công") ? 'success' : 'danger', message);
                    if (message.includes("thành công")) {
                        accountForm.reset();
                        setTimeout(() => window.location.href = "/admin/index/listTaiKhoan", 1000);
                    }
                })
                .catch(error => showAlert('danger', error.message || "Có lỗi xảy ra khi tạo tài khoản!"))
                .finally(() => {
                    isSubmitting = false;
                    submitButton.disabled = false;
                });
        }

        // Hàm hiển thị thông báo (giữ lại trong file JS nếu cần dùng chung)
        function showAlert(type, message) {
            const alertDiv = document.createElement('div');
            alertDiv.className = `alert alert-${type} alert-dismissible fade show`;
            alertDiv.style.zIndex = '1000';
            alertDiv.innerHTML = `
                <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                <p>${message}</p>
            `;
            const content = document.querySelector('.content');
            const box = document.querySelector('.box');
            if (content && box) {
                content.insertBefore(alertDiv, box);
            } else {
                document.body.prepend(alertDiv);
            }
            setTimeout(() => alertDiv.remove(), 5000);
        }

        // Gắn sự kiện
        if (accountForm && submitButton) {
            accountForm.addEventListener("submit", addTaiKhoan);
        }
    });
    document.addEventListener('DOMContentLoaded', function() {
        fetch('/admin/listTaiKhoan', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            }
        })
            .then(response => {
                console.log('Response status:', response.status);
                if (!response.ok) {
                    throw new Error(`HTTP error! Status: ${response.status}`);
                }
                return response.json();
            })
            .then(data => {
                console.log('Data received:', data);
                if (!Array.isArray(data)) {
                    throw new Error('Dữ liệu trả về không phải là mảng');
                }

                const admins = data.filter(item => item.account_type === 'Admin');
                const giangViens = data.filter(item => item.account_type === 'GiangVien');
                const sinhViens = data.filter(item => item.account_type === 'SinhVien');

                populateTable(admins, 'admin-table');
                populateTable(giangViens, 'giangvien-table');
                populateTable(sinhViens, 'sinhvien-table');
            })
            .catch(error => {
                console.error('Lỗi khi lấy dữ liệu:', error);
                showAlert('danger', `Lỗi khi tải dữ liệu tài khoản: ${error.message}`);
            });
    });


}