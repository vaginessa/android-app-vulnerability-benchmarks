function before_install_vuln() {
  $webServer = Start-Process powershell "cd ..\..\Misc\LocalServer; python.exe .\index_https.py" -PassThru
}

function before_test_vuln() {
  #put any task that needs to be performed before testing vulnerable
}

function after_uninstall_vuln() {
  #put any task that needs to be performed after uninstalling vulnerable
}

function before_install_secure() {
  #put any task that needs to be performed before installing secure
}

function before_test_secure() {
  #put any task that needs to be performed before testing secure
}

function after_uninstall_secure() {
  $web = New-Object Net.WebClient
  [System.Net.ServicePointManager]::ServerCertificateValidationCallback = { $true }
  $web.DownloadString("https://localhost:5000/shutdown")
  [System.Net.ServicePointManager]::ServerCertificateValidationCallback = $null
  echo "killed the web server"
}
