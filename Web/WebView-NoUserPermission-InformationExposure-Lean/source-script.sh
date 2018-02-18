function before_install_vuln() {
  cd ../../Misc/LocalServer/
  python index_http.py &
  echo "benchmark path = $1"
  cd $1
}

function before_test_vuln() {
  #put any task that needs to be performed before testing vulnerable
  :
}

function after_uninstall_vuln() {
  #put any task that needs to be performed after uninstalling vulnerable
  :
}

function before_install_secure() {
  #put any task that needs to be performed before installing secure
  :
}

function before_test_secure() {
  #put any task that needs to be performed before testing secure
  :
}

function after_uninstall_secure() {
  curl http://localhost:5000/shutdown
}
