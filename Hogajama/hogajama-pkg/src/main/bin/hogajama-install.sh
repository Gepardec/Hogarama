PKG_DIR=/root

mkdir $PKG_DIR/config/deployments
cp $PKG_DIR/deployments/*.war $PKG_DIR/config/deployments
cd $PKG_DIR/bin
$PKG_DIR/bin/install.sh -r hogajama
