# POC

This is just for demonstration purpose and should not be abused for any kind of malicious actions.

See this [video](https://www.youtube.com/watch?v=DOy3AReZZes) for demonstration of this attack.

## AppLink over-permission

This app abuses the fact that if an app registers two sites with the same scheme, e.g. "https://example.org" and "https://facebook.org", and a user allows this apps to always open links for https://example.org then android will also open all facebook links automatically with this app without prompting the user.

More details about this vulnerability can be found [here](https://www.usenix.org/system/files/conference/usenixsecurity17/sec17-liu.pdf).

## Example target: FACEBOOK App

All apps that do not correctly verify their AppLinks are vulnerable to this attack. Victims can easily be tricked and their credentials can be stolen. This POC uses the facebook app as an example because it does not verify its links. As soon as the victim clicks on "Always" when this app prompts for it, all facebook links that are clicked in the Chrome browser will automatically open this app and then this app tries to steal the credentials of the user.

This is just a proof of concept and of course, the app can be designed better to increase the chances of success. 


## Disclosure

Google and Facebook are aware of the vulnerability, but do not feel that it needs to be patched immediately. I think that even experienced users can be tricked in this way so this just definetly be fixed asap.


