db.createUser({
    user: 'hogajama',
    pwd: 'changepassword',
    roles: [
        {
            role: 'dbOwner',
            db: 'hogajamadb',
        },
    ],
});
