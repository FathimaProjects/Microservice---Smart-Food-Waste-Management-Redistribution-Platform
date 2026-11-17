<#
.SYNOPSIS
  Launches an individual microservice for testing.
.DESCRIPTION
  Allows team members to run only their assigned microservice independently.
.PARAMETER Service
  Optional service name (user, provider, listing, order, payment, notification)
#>
param(
    [string]$Service
)

$serviceMap = @{
    "1" = @{ Name = "User Service"; Dir = "user-service"; Port = 8081; Key = "user" }
    "2" = @{ Name = "Food Provider Service"; Dir = "food-provider-service"; Port = 8082; Key = "provider" }
    "3" = @{ Name = "Food Listing Service"; Dir = "food-listing-service"; Port = 8083; Key = "listing" }
    "4" = @{ Name = "Order Service"; Dir = "order-service"; Port = 8084; Key = "order" }
    "5" = @{ Name = "Payment Service"; Dir = "payment-service"; Port = 8085; Key = "payment" }
    "6" = @{ Name = "Notification Service"; Dir = "notification-service"; Port = 8086; Key = "notification" }
}

$selected = $null

if ($Service) {
    foreach ($entry in $serviceMap.Values) {
        if ($entry.Key -ieq $Service -or $entry.Dir -ieq $Service) {
            $selected = $entry
            break
        }
    }
}

if (-not $selected) {
    Write-Host "==========================================================" -ForegroundColor Green
    Write-Host "  RescueBite - Launch Single Microservice" -ForegroundColor Green
    Write-Host "==========================================================" -ForegroundColor Green
    Write-Host "Select which service you want to run:" -ForegroundColor Cyan
    Write-Host "  [1] User Management Service       (Port 8081)" -ForegroundColor White
    Write-Host "  [2] Food Provider Service         (Port 8082)" -ForegroundColor White
    Write-Host "  [3] Food Listing Service          (Port 8083)" -ForegroundColor White
    Write-Host "  [4] Order Service                 (Port 8084)" -ForegroundColor White
    Write-Host "  [5] Payment Service               (Port 8085)" -ForegroundColor White
    Write-Host "  [6] Notification Service          (Port 8086)" -ForegroundColor White
    Write-Host ""
    $choice = Read-Host "Enter service number (1-6)"
    $selected = $serviceMap[$choice]
}

if ($selected) {
    Write-Host ""
    Write-Host "Starting $($selected.Name) on port $($selected.Port)..." -ForegroundColor Yellow
    Write-Host "Swagger UI will be available at: http://localhost:$($selected.Port)/swagger-ui.html" -ForegroundColor Cyan
    Write-Host ""
    
    $dirPath = Join-Path $PSScriptRoot $selected.Dir
    Set-Location $dirPath
    mvn spring-boot:run
} else {
    Write-Host "Invalid choice. Please run the script again and select 1 through 6." -ForegroundColor Red
}
