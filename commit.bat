git add .
set /p commitmsg=Enter Commit Message: 
git commit -am "%commitmsg%"
git push --force origin main
pause