# Android Student Management System
## Introduction
This is a simple Android application for managing student records, providing basic features such as adding, deleting, editing, and updating records through a RESTful API, implemented using the MVVM architecture.
## Screenshots
## Built With
- ViewModel, DataBinding, LiveData, and other components to simplify MVVM architecture implementation.
- Navigation component for navigating between fragments and using Safe Args to pass arguments.
- Retrofit for simplifying communication with RESTful APIs.
- RecyclerView for displaying the list of students.

## Database
![ERD](https://i.imgur.com/SMXUc1R.png)

## APIs
**Written in PHP**
<details>
<summary>login.php</summary>

```php
<?php
	require "dbConnect.php";

	$user = $_POST['user'];
	$password = $_POST['password'];
	if($connect) {
		$query = "SELECT * FROM admin WHERE admin.user = ? AND admin.password = ?;";
		$stmt = mysqli_prepare($connect, $query);
		mysqli_stmt_bind_param($stmt, "ss", $user, $password);
		mysqli_execute($stmt);
		$data = mysqli_stmt_get_result($stmt);
		if(mysqli_num_rows($data) == 1) {
			echo "login_success";
		} else {
			echo "login_fail";
		}
	} else {
		echo "connect_error";
	}
?>
```
</details>

<details>
<summary>uploadImage.php</summary>

```php
<?php
	$file_path = "image/";
	$file_path = $file_path.basename($_FILES['uploaded_file']['name']);

	if (move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $file_path)) {
		echo $_FILES['uploaded_file']['name'];
	} else {
		echo "Error";
	}
?>
```
</details>

<details>
<summary>insert.php</summary>

```php
<?php
	require "dbConnect.php";

	if ($connect) {
		$id_class = $_POST['id_class'];
		$full_name = $_POST['full_name'];
		$nick_name = $_POST['nick_name'];
		$birthday = $_POST['birthday'];
		$gender = $_POST['gender'];
		$avatar = $_POST['avatar'];

		$query = "INSERT INTO student(id_class, full_name, nick_name, birthday, gender, avatar) 
			VALUES(?, ?, ?, ?, ?, ?)";
        
        $stmt = mysqli_prepare($connect, $query);
        mysqli_stmt_bind_param($stmt, "isssss", $id_class, $full_name, $nick_name, $birthday, $gender, $avatar);
        $result = mysqli_execute($stmt);
	        
		if($result) {
			echo "insert_success";
		} else {
			echo "insert_error";
		} 
	} else {
		echo "connect_error";
	}

?>
```
</details>

<details>
<summary>edit.php</summary>

```php
<?php
	require "dbConnect.php";

	if ($connect) {
		$id_student = $_POST['id_student'];
		$id_class = $_POST['id_class'];
		$full_name = $_POST['full_name'];
		$nick_name = $_POST['nick_name'];
		$birthday = $_POST['birthday'];
		$birthday = date("Y-m-d", strtotime(str_replace('/', '-', $birthday)));
		$gender = $_POST['gender'];
		$avatar = $_POST['avatar'];
		$address = $_POST['address'];

		$query = "UPDATE student
			SET id_class = ?,
			full_name = ?,
			nick_name = ?,
			birthday = ?,
			gender = ?,
			avatar = ?,
			address = ?
			WHERE id_student = ?;";

		$stmt = mysqli_prepare($connect, $query);
		mysqli_stmt_bind_param($stmt, "issssssi", $id_class, $full_name, $nick_name, $birthday, $gender, $avatar, $address, $id_student);
        $result = mysqli_execute($stmt);
	        
		if($result) {
			echo "edit_success";
		} else {
			echo "edit_error";
		} 
	} else {
		echo "connect_error";
	}

?>
```
</details>

<details>
<summary>deleteStudent.php</summary>

```php
<?php
	require 'dbConnect.php';

	if ($connect) {
		$id_student = $_POST["id_student"];

		$query = "DELETE FROM student WHERE id_student = ?;";
		$stmt = mysqli_prepare($connect, $query);
		mysqli_stmt_bind_param($stmt, "i", $id_student);
		$result = mysqli_execute($stmt);
	        
		if($result) {
			echo "delete_success";
		} else {
			echo "delete_error";
		} 
	} else {
		echo "connect_error";
	}
?>
```
</details>

<details>
<summary>getListStudents.php</summary>

```php
<?php

require 'dbConnect.php';
if ($connect) {
	$query = "
	SELECT student.id_student, student.full_name, student.nick_name, student.gender, getMonthOld(student.birthday) AS months,
		student.avatar, student.birthday, student.address, class.name
	FROM student
	JOIN class ON student.id_class = class.id_class;
	";
	$data = mysqli_query($connect, $query);

	// 1. Tạo class Student
	/**
	 * 
	 */
	class Student {
		public $id;
		public $fullName;
		public $nickName;
		public $gender;
		public $age;
		public $avatar;
		public $birthday;
		public $address;
		public $class;

		
		public function __construct($id, $fullName, $nickName, $gender, $age, $avatar, $birthday, $address, $class) {
			$this->id = $id;
			$this->fullName = $fullName;
			$this->nickName = $nickName;
			$this->age = $age;
			$this->gender = $gender;
			$this->avatar = $avatar;
			$birthday = date("d/m/Y", strtotime($birthday));
			$this->birthday = $birthday;
			$this->address = $address;
			$this->class = $class;
		}
	}

	// 2. Tạo mảng
	$listStudents = array();

	// 3. Thêm phần tử vào mảng
	while ($row = mysqli_fetch_assoc($data)) {
		array_push($listStudents, new Student($row['id_student'], $row['full_name'], $row['nick_name'], $row['gender'], intdiv($row['months'], 12), 
			$row['avatar'], $row['birthday'], $row['address'], $row['name']));
	}

	// 4. Chuyển định dạng của mảng -> JSON
	echo json_encode($listStudents);
} else {
	echo "connect_error";
}


?>
```
</details>

<details>
<summary>getStudent.php</summary>

```php
<?php

require 'dbConnect.php';
if ($connect) {
	$id_student = $_POST['id_student'];

	$query = "
	SELECT student.id_student, student.full_name, student.nick_name, student.gender, getMonthOld(student.birthday) AS months,
		student.avatar, student.birthday, student.address, class.name
	FROM student
	JOIN class ON student.id_class = class.id_class
	WHERE student.id_student = ?;
	";
	$stmt = mysqli_prepare($connect, $query);
	mysqli_stmt_bind_param($stmt, "i", $id_student);
	mysqli_execute($stmt);
	$data = mysqli_stmt_get_result($stmt);

	// 1. Tạo class Student
	/**
	 * 
	 */
	class Student {
		public $id;
		public $fullName;
		public $nickName;
		public $gender;
		public $age;
		public $avatar;
		public $birthday;
		public $address;
		public $class;

		
		public function __construct($id, $fullName, $nickName, $gender, $age, $avatar, $birthday, $address, $class) {
			$this->id = $id;
			$this->fullName = $fullName;
			$this->nickName = $nickName;
			$this->age = $age;
			$this->gender = $gender;
			$this->avatar = $avatar;
			$birthday = date("d/m/Y", strtotime($birthday));
			$this->birthday = $birthday;
			$this->address = $address;
			$this->class = $class;
		}
	}

	$row = mysqli_fetch_array($data, MYSQLI_ASSOC);
	$student = new Student($row['id_student'], $row['full_name'], $row['nick_name'], $row['gender'], intdiv($row['months'], 12), 
			$row['avatar'], $row['birthday'], $row['address'], $row['name']);

	echo json_encode($student);
	
} else {
	echo "connect_error";
}


?>
```
</details>

Work In Progress ... 
