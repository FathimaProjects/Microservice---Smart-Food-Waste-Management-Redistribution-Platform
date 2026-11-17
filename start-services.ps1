# RescueBite - Windows PowerShell Launcher
# Starts all 6 microservices in separate PowerShell windows

Write-Host "==========================================================" -ForegroundColor Green
Write-Host "  RescueBite - Launching Microservices" -ForegroundColor Green
Write-Host "==========================================================" -ForegroundColor Green

$services = @(
    @{ Name = "User Service"; Dir = "user-service"; Port = 8081 },
    @{ Name = "Food Provider Service"; Dir = "food-provider-service"; Port = 8082 },
    @{ Name = "Food Listing Service"; Dir = "food-listing-service"; Port = 8083 },
    @{ Name = "Order Service"; Dir = "order-service"; Port = 8084 },
    @{ Name = "Payment Service"; Dir = "payment-service"; Port = 8085 },
    @{ Name = "Notification Service"; Dir = "notification-service"; Port = 8086 }
)

foreach ($s in $services) {
    Write-Host "Starting $($s.Name) on Port $($s.Port)..." -ForegroundColor Yellow
    $dirPath = Join-Path $PSScriptRoot $s.Dir
    Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$dirPath'; Write-Host '--- $($s.Name) (Port $($s.Port)) ---' -ForegroundColor Cyan; mvn spring-boot:run"
    Start-Sleep -Seconds 2
}

Write-Host ""
Write-Host "All 6 microservices triggered!" -ForegroundColor Green
Write-Host "Access Swagger UI dashboards once started:" -ForegroundColor White
Write-Host "  - User Service:         http://localhost:8081/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Food Provider Service:http://localhost:8082/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Food Listing Service: http://localhost:8083/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Order Service:        http://localhost:8084/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Payment Service:      http://localhost:8085/swagger-ui.html" -ForegroundColor Gray
Write-Host "  - Notification Service: http://localhost:8086/swagger-ui.html" -ForegroundColor Gray

