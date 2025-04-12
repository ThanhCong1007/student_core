document.addEventListener('DOMContentLoaded', function () {
    // Lấy maGV và maMH từ các biến được truyền vào từ Thymeleaf
    const maGV = document.getElementById('maGV').value;
    const maMH = document.getElementById('maMH').value;

    // Hàm lấy danh sách sinh viên
    function fetchSinhVienList() {
        fetch(`/giangvien/monhoc/${maGV}/${maMH}/sinhvien-list`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error('Không thể lấy danh sách sinh viên');
                }
                return response.json();
            })
            .then(data => {
                const tbody = document.getElementById('sinhVienTableBody');
                tbody.innerHTML = ''; // Xóa nội dung hiện tại của bảng

                if (data.length === 0) {
                    const row = document.createElement('tr');
                    row.innerHTML = '<td colspan="5" style="text-align: center;">Không có sinh viên nào</td>';
                    tbody.appendChild(row);
                } else {
                    data.forEach(sinhVien => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>${sinhVien.maSV}</td>
                        <td>${sinhVien.tenSV}</td>
                        <td>${sinhVien.tenGV}</td>
                        <td>${sinhVien.maMH}</td>
                        <td>
                            <a href="/giangvien/diem/${maGV}/${maMH}/${sinhVien.maSV}" class="btn btn-info btn-sm">Xem điểm</a>
                           
                        </td>
                    `;
                        tbody.appendChild(row);
                    });
                }
            })
            .catch(error => {
                console.error('Lỗi:', error);
                const tbody = document.getElementById('sinhVienTableBody');
                tbody.innerHTML = '<tr><td colspan="5" style="text-align: center;">Lỗi khi tải danh sách sinh viên</td></tr>';
            });
    }

    // Gọi lần đầu khi trang tải
    fetchSinhVienList();


});