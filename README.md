# STRANGERS ALLIENS Editor APK (Android 15)

This Android WebView app opens the private Supabase-backed editor route at:
`https://catalogo-virtual-strangers.samas-musick.workers.dev/editor.html`.
It supports the Android document picker for multi-photo and video uploads. Catalog edits write to Supabase, so the public website reads the same data.

## Build without installing Android Studio/Node locally

The repository includes `.github/workflows/build-apk.yml`. Upload the contents of this folder to a GitHub repository. GitHub Actions builds a debug APK with `targetSdk 35` and publishes it as a downloadable workflow artifact.

1. Create a new GitHub repository in your browser.
2. Upload the project files and folders from this directory to the repository root, including `.github/workflows/build-apk.yml`.
3. Open the repository's **Actions** tab and run **Build STRANGERS ALLIENS APK** (or let the push to `main` start it).
4. Open the completed workflow run, download artifact `STRANGERS-ALLIENS-Android15-APK`, unzip it, and install `app-debug.apk` on Android 15.

This is a debug-signed APK intended for direct installation/testing, not Play Store publication. The app displays the private editor page; the public catalog has no admin button. Product/content updates from Supabase sync to the website without reinstalling the APK. App-code updates are separate.

The APK needs an internet connection. Do not place Supabase secret/service-role keys in this project; it uses the authenticated editor page and the public Supabase publishable key configured on the website.
