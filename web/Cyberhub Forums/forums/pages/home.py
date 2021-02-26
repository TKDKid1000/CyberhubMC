from flask_login import current_user
from flask import render_template

from ..loaduser import loaduser
def homepage():
    if current_user.is_authenticated:
        return render_template("homepage.html", data=loaduser(current_user.id))
    else:
        return render_template("homepage.html")