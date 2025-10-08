<?php
$host = 'localhost';
$dbname = 'taskmanager';
$username = 'root';
$password = '';

try {    
    $pdo = new PDO("mysql:host=$host;dbname=$dbname;charset=utf8mb4", $username, $password);  
    $pdo->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
    
    // Успешное подключение
    echo "Подключение к БД успешно!";
    
} catch (PDOException $e) {    
    die(json_encode(['error' => 'Ошибка подключения: ' . $e->getMessage()]));
}
?>