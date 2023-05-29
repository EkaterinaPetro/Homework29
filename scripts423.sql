SELECT student.name, student.age, faculty.name
FROM student
INNER JOIN faculty ON student.faculty_id = faculty.id;

SELECT student.name, student.age, avatar.id
FROM student
INNER JOIN avatar on student.id = avatar.student_id;