// Khi DOM đã sẵn sàng
document.addEventListener('DOMContentLoaded', function () {
    // Hàm hiển thị section tương ứng với hiệu ứng chuyển cảnh
    function showSection(sectionId) {
        const sections = document.querySelectorAll('.content-section');
        sections.forEach(section => {
            section.classList.remove('active');
            section.style.position = 'absolute';
            section.style.opacity = '0';
            section.style.pointerEvents = 'none';
            section.style.right = '250px'; // hiệu ứng trượt ra
        });

        const activeSection = document.getElementById(sectionId);
        if (activeSection) {
            activeSection.classList.add('active');
            activeSection.style.position = 'relative';
            activeSection.style.opacity = '1';
            activeSection.style.pointerEvents = 'auto';
            activeSection.style.left = '0';
        }
    }

    // Gắn sự kiện click cho sidebar menu
    document.querySelectorAll('.sidebar ul li a').forEach(link => {
        link.addEventListener('click', function (e) {
            e.preventDefault();
            const sectionId = this.getAttribute('onclick').match(/'(.+?)'/)[1];
            showSection(sectionId);
        });
    });

    // Xử lý form Cấp tài khoản
    document.getElementById("account-form").addEventListener("submit", function(event) {
        event.preventDefault();

        // Lấy dữ liệu từ form
        const data = {
            role: document.getElementById("account-type").value,
            username: document.getElementById("username").value,
            email: document.getElementById("email").value,
            password: document.getElementById("password").value
        };

        // Gửi API bằng Fetch
        fetch("http://localhost:8080/admin/addTaiKhoan", {
            method: "POST",
            headers: { "Content-Type": "application/json;charset=UTF-8" },
            body: JSON.stringify(data)
        })
            .then(response => response.text())
            .then(message => {
                alert(message);
                document.getElementById("account-form").reset(); // Reset form sau khi tạo tài khoản
            })
            .catch(error => {
                alert("error");
                console.error("Loi:", error);
            });
    });

    // Xử lý form Quản lý điểm
    document.getElementById('score-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const studentId = document.getElementById('student-id').value;
        const subjectId = document.getElementById('subject-id').value;
        const score = document.getElementById('score').value;

        console.log(`Cập nhật điểm: Mã sinh viên: ${studentId}, Mã môn học: ${subjectId}, Điểm: ${score}`);
        alert('Điểm đã được cập nhật thành công!');
        this.reset();
    });

    // Xử lý form Quản lý môn học
    document.getElementById('subject-form').addEventListener('submit', function (event) {
        event.preventDefault();
        const subjectName = document.getElementById('subject-name').value;
        const subjectCode = document.getElementById('subject-code').value;

        console.log(`Thêm môn học: ${subjectName}, Mã môn học: ${subjectCode}`);
        alert('Môn học đã được thêm thành công!');
        this.reset();
    });

    // Giả lập thống kê người dùng
    document.getElementById('student-count').textContent = 120;
    document.getElementById('teacher-count').textContent = 25;
    document.getElementById('subject-count').textContent = 18;

    // Mặc định hiển thị "Cấp tài khoản"
    showSection('account-management');
});
