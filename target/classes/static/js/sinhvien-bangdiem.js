document.addEventListener('DOMContentLoaded', function () {
    const maSV = document.getElementById('maSV').value;

    // Hàm lấy danh sách điểm
    function fetchDiemList() {
        fetch(`/sinhvien/${maSV}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                if (!response.ok) {
                    throw new Error(response.statusText);
                }
                return response.json();
            })
            .then(data => {
                const tbody = document.getElementById('diemTableBody');
                tbody.innerHTML = ''; // Xóa nội dung hiện tại của bảng

                if (data.length === 0) {
                    const row = document.createElement('tr');
                    row.innerHTML = '<td colspan="7" style="text-align: center;">Không có điểm nào</td>';
                    tbody.appendChild(row);
                } else {
                    data.forEach(diem => {
                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>${diem.maSV}</td>
                        <td>${diem.tenSV}</td>
                        <td>${diem.maMH}</td>
                        <td>${diem.tenMH}</td>
                        <td>${diem.tenGV}</td>
                        <td>${diem.soTinChi}</td>
                        <td>${diem.diem !== null ? diem.diem : 'Chưa có điểm'}</td>
                    `;
                        tbody.appendChild(row);
                    });
                }
            })
            .catch(error => {
                console.error('Lỗi:', error);
                const tbody = document.getElementById('diemTableBody');
                tbody.innerHTML = '<tr><td colspan="7" style="text-align: center;">Lỗi khi tải bảng điểm</td></tr>';
            });
    }

    // Gọi lần đầu khi trang tải
    fetchDiemList();
});