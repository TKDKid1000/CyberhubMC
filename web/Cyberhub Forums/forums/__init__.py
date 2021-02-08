import os
import json
from flask import Flask, request, render_template, flash, redirect, url_for
from flask_login import LoginManager
import flask_login
from .loaduser import loadusers

def create_app(test_config=None):
    app = Flask(__name__, instance_relative_config=True, template_folder='template')

    app.secret_key = b'eiajdoiajd_awd\wdawdadfksoek'
    login_manager = LoginManager()
    login_manager.init_app(app)

    try:
        os.makedirs(app.instance_path)
    except OSError:
        pass
    app.url_map.strict_slashes = False
    
    class User(flask_login.UserMixin):
        pass

    if not os.path.exists("posts"):
        os.makedirs("posts")

    with open("accounts.json") as f:
        users = json.load(f)
    
    from .pages import newpost
    from .pages import home
    from .pages import viewpost
    from .pages import forumshome
    from .pages import news
    from .pages import account
    app.add_url_rule("/", view_func=home.homepage)
    app.add_url_rule("/home", view_func=home.homepage)
    app.add_url_rule("/forums", view_func=forumshome.forumhomepage)
    app.add_url_rule("/forums/post", view_func=newpost.newforumpost, methods=["GET", "POST"])
    app.add_url_rule("/forums/view/<postid>", view_func=viewpost.viewforumpost, methods=["GET", "POST"])
    app.add_url_rule("/news", view_func=news.newspage, methods=["GET", "POST"])
    app.add_url_rule("/account", view_func=account.accountpage, methods=["GET", "POST"])

    @app.errorhandler(404)
    def notfounderror(e):
        return render_template("404.html"), 404

    @app.errorhandler(401)
    def unauthorizederror(e):
        return redirect(url_for("login"))

    @login_manager.user_loader
    def load_user(email):
        if email not in loadusers():
            return
        user = User()
        user.id = email
        return user

    @login_manager.request_loader
    def load_request(request):
        email = request.form.get("email")
        if email not in users:
            return
        user = User()
        user.id = email
        user.name = users[email]["username"]
        user.is_authenticated = request.form["password"] == users[email]["password"]
        return user
    
    @app.route('/login', methods=['GET', 'POST'])
    def login():
        if request.method == "GET":
            return render_template("login.html")
        email = request.form["email"]
        if email in loadusers():
            if request.form["password"] == loadusers()[email]["password"]:
                user = User()
                user.id = email
                flask_login.login_user(user)
                return redirect(url_for("homepage"))
            else:
                return "incorrect password. <a href='%s'>Try Again</a>" % url_for('login')
        else:
            return "incorrect email <a href='%s'>Try Again</a>" % url_for('login')
    
    @app.route("/register", methods=["GET", "POST"])
    def register():
        if request.method == "GET":
            return render_template("register.html")
        email = request.form["email"]
        if not email in users:
            loadusers()[email] = {"username" : request.form["username"], "password" : request.form["password"], "icon" : "https://i.imgur.com/y1MomN8.png"}
            with open("accounts.json", "w") as f:
                json.dump(users, f)
                return redirect(url_for("login"))
        else:
            return "that email is already taken. <a href='%s'>Try Another</a>" % url_for('register')
    
    @app.route("/logout")
    def logout():
        flask_login.logout_user()
        return redirect(url_for("login"))
    return app
