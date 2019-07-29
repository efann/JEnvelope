tell application "Finder"
	set lcCurrentPath to POSIX path of ((container of (path to me)) as string)
end tell

set lcApplication to "java -splash:" & lcCurrentPath & "Beowurks.png -Xdock:icon=" & lcCurrentPath & "JEnvelope.jpg -jar \"" & lcCurrentPath & "JEnvelope.jar\""

do shell script lcApplication & " &>/dev/null &"

