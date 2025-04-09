document.addEventListener('DOMContentLoaded', function () {
    const scoreForm = document.getElementById('score-form');
    if (scoreForm) {
        scoreForm.addEventListener('submit', function (event) {
            event.preventDefault();
            const studentId = document.getElementById('student-id').value;
            const subjectId = document.getElementById('subject-id').value;
            const score = document.getElementById('score').value;

            console.log(`Cập nhật điểm: Mã SV: ${studentId}, Mã môn học: ${subjectId}, Điểm: ${score}`);
            alert('Điểm đã được cập nhật thành công!');
            scoreForm.reset();
        });
    }
});
