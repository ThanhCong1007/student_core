document.addEventListener('DOMContentLoaded', function () {
    // Xử lý form cập nhật thông tin giảng viên
    const updateForm = document.getElementById('updateForm');
    if (updateForm) {
        updateForm.addEventListener('submit', function(event) {
            event.preventDefault();

            // Lấy dữ liệu từ form
            const maGV = document.getElementById('maGVInput').value;
            const tenGV = document.getElementById('tenGVInput').value;
            const email = document.getElementById('emailInput').value;

            // Tạo object dữ liệu để gửi
            const data = {
                maGV: parseInt(maGV),
                tenGV: tenGV,
                email: email
            };

            // Gửi request POST tới endpoint
            fetch('/giangvien/update', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(data)
            })
                .then(response => {
                    if (!response.ok) {
                        return response.json().then(err => { throw new Error(err.message); });
                    }
                    return response.json();
                })
                .then(json => {
                    alert(json.message || 'Cập nhật thông tin giảng viên thành công!');
                    window.location.reload(); // Làm mới trang để hiển thị thông tin mới
                })
                .catch(error => {
                    alert(error.message || 'Có lỗi xảy ra khi cập nhật thông tin!');
                    console.error('Lỗi:', error);
                });
        });
    }
});