function before_install_vuln() {
  cd ../../Misc/LocalServer/
  python index_http.py &
  echo "benchmark path = $1"
  cd $1
}

function before_test_vuln() {
  if [ -z $ANDROID_HOME ] ; then
    echo "Please set ANDROID_HOME env variable."
    exit -1
  fi
  $ANDROID_HOME/platform-tools/adb root
  $ANDROID_HOME/platform-tools/adb push demo1.html /data/data/edu.ksu.cs.benign/files/demo1.html
  $ANDROID_HOME/platform-tools/adb push File2 /data/data/edu.ksu.cs.benign/files/File2
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
  if [ -z $ANDROID_HOME ] ; then
    echo "Please set ANDROID_HOME env variable."
    exit -1
  fi
  $ANDROID_HOME/platform-tools/adb root
  $ANDROID_HOME/platform-tools/adb push demo1.html /data/data/edu.ksu.cs.benign/files/demo1.html
  $ANDROID_HOME/platform-tools/adb push File2 /data/data/edu.ksu.cs.benign/files/File2
}

function after_uninstall_secure() {
  curl http://localhost:5000/shutdown
}
