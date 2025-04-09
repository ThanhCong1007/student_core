document.addEventListener('DOMContentLoaded', function () {
    // Lấy maGV từ biến được truyền vào (nếu có)
    const maGV = document.getElementById('maGV')?.value;

    // Hàm xử lý thêm môn học
    function handleAddMonHoc() {
        const addForm = document.getElementById("addForm");
        if (addForm) {
            addForm.addEventListener("submit", function(event) {
                event.preventDefault();

                const tenMH = document.getElementById("tenMH").value;
                const soTinChi = document.getElementById("soTinChi").value;
                const maGVInput = document.getElementById("maGV").value;

                const data = {
                    tenMH: tenMH,
                    soTinChi: parseInt(soTinChi),
                    maGV: maGVInput ? parseInt(maGVInput) : null
                };

                fetch('/api/monhoc', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => {
                        if (!response.ok) {
                            return response.json().then(err => { throw new Error(err.message); });
                        }
                        return response.json();
                    })
                    .then(data => {
                        const message = data.message || "Thêm môn học thành công!";
                        const alertDiv = document.createElement('div');
                        alertDiv.className = 'alert alert-success alert-dismissible';
                        alertDiv.innerHTML = `
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                            <p>${message}</p>
                        `;
                        document.querySelector('.content')?.insertBefore(alertDiv, document.querySelector('.box'));

                        addForm.reset();
                        if (message.includes("thành công")) {
                            setTimeout(() => {
                                window.location.href = "/admin/monhoc";
                            }, 1000);
                        }
                    })
                    .catch(error => {
                        const alertDiv = document.createElement('div');
                        alertDiv.className = 'alert alert-danger alert-dismissible';
                        alertDiv.innerHTML = `
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                            <p>${error.message || "Có lỗi xảy ra khi thêm môn học!"}</p>
                        `;
                        document.querySelector('.content')?.insertBefore(alertDiv, document.querySelector('.box'));
                        console.error("Lỗi:", error);
                    });
            });
        }
    }

    // Hàm xử lý sửa môn học
    function handleEditMonHoc() {
        const editForm = document.getElementById("editForm");
        if (editForm) {
            editForm.addEventListener("submit", function(event) {
                event.preventDefault();

                const maMH = document.getElementById("maMH").value;
                const tenMH = document.getElementById("tenMH").value;
                const soTinChi = document.getElementById("soTinChi").value;
                const maGVInput = document.getElementById("maGV").value;

                const data = {
                    maMH: parseInt(maMH),
                    tenMH: tenMH,
                    soTinChi: parseInt(soTinChi),
                    maGV: maGVInput ? parseInt(maGVInput) : null
                };

                fetch(`/api/monhoc/${maMH}`, {
                    method: 'PUT',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(data)
                })
                    .then(response => {
                        if (!response.ok) {
                            return response.json().then(err => { throw new Error(err.message); });
                        }
                        return response.json();
                    })
                    .then(data => {
                        const message = data.message || "Cập nhật môn học thành công!";
                        const alertDiv = document.createElement('div');
                        alertDiv.className = 'alert alert-success alert-dismissible';
                        alertDiv.innerHTML = `
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                            <p>${message}</p>
                        `;
                        document.querySelector('.content')?.insertBefore(alertDiv, document.querySelector('.box'));

                        if (message.includes("thành công")) {
                            setTimeout(() => {
                                window.location.href = "/admin/monhoc";
                            }, 1000);
                        }
                    })
                    .catch(error => {
                        const alertDiv = document.createElement('div');
                        alertDiv.className = 'alert alert-danger alert-dismissible';
                        alertDiv.innerHTML = `
                            <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
                            <p>${error.message || "Có lỗi xảy ra khi cập nhật môn học!"}</p>
                        `;
                        document.querySelector('.content')?.insertBefore(alertDiv, document.querySelector('.box'));
                        console.error("Lỗi:", error);
                    });
            });
        }
    }

    // Gọi các hàm khi trang tải
    handleAddMonHoc();
    handleEditMonHoc();
});