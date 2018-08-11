function before_install_vuln() {
  $webServer = Start-Process powershell "cd ..\..\Misc\LocalServer; python.exe .\index_http.py" -PassThru
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
  Invoke-WebRequest -Uri "http://localhost:5000/shutdown"
  echo "killed the web server"
}
