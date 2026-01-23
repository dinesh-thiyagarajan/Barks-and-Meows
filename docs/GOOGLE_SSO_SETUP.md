# Google SSO Setup Guide

This guide will help you set up Google Sign-In for the Barks and Meows app.

## Prerequisites
- Firebase project already configured
- Firebase Authentication enabled

## Android Setup

### 1. Enable Google Sign-In in Firebase Console

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Navigate to **Authentication** → **Sign-in method**
4. Click on **Google** and enable it
5. Add your support email
6. Click **Save**

### 2. Get your Web Client ID

1. In Firebase Console, go to **Project Settings** (gear icon)
2. Scroll down to **Your apps** section
3. Find your Web app (or create one if it doesn't exist)
4. Copy the **Web client ID** (looks like: `123456789-abcdefg.apps.googleusercontent.com`)

### 3. Configure the App

1. Open `/composeApp/src/androidMain/kotlin/auth/GoogleSignInButton.android.kt`
2. Replace `YOUR_WEB_CLIENT_ID` with your actual Web Client ID:
   ```kotlin
   private const val WEB_CLIENT_ID = "123456789-abcdefg.apps.googleusercontent.com"
   ```

### 4. Add SHA-1 Fingerprint (Required for Android)

1. Get your SHA-1 fingerprint:
   ```bash
   # Debug key
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

   # Release key (when you have one)
   keytool -list -v -keystore /path/to/your/keystore.jks -alias your-key-alias
   ```

2. Copy the SHA-1 fingerprint

3. In Firebase Console:
   - Go to **Project Settings**
   - Select your Android app
   - Click **Add fingerprint**
   - Paste your SHA-1 fingerprint
   - Click **Save**

### 5. Test the Integration

1. Run the app on an Android device/emulator
2. Navigate to the Login screen
3. Click **Sign in with Google**
4. Select your Google account
5. You should be logged in!

## iOS Setup

### 1. Install GoogleSignIn Pod

1. Add to your `iosApp/Podfile`:
   ```ruby
   pod 'GoogleSignIn'
   ```

2. Run:
   ```bash
   cd iosApp
   pod install
   ```

### 2. Configure URL Schemes

1. Get your iOS Client ID from Firebase Console
2. Get your REVERSED_CLIENT_ID (format: `com.googleusercontent.apps.123456789`)
3. Add to `iosApp/Info.plist`:
   ```xml
   <key>CFBundleURLTypes</key>
   <array>
       <dict>
           <key>CFBundleURLSchemes</key>
           <array>
               <string>YOUR_REVERSED_CLIENT_ID</string>
           </array>
       </dict>
   </array>
   ```

### 3. Implement iOS Sign-In

Update `/auth/src/iosMain/kotlin/com/dineshworkspace/auth/platform/GoogleSignInHelper.ios.kt` with proper Google Sign-In iOS implementation using GIDSignIn.

## Troubleshooting

### Android Issues

**Error: "Developer Error" or "API not enabled"**
- Make sure you added the correct SHA-1 fingerprint to Firebase
- Verify Google Sign-In is enabled in Firebase Console
- Wait a few minutes after adding SHA-1 (can take time to propagate)

**Error: "Web Client ID is invalid"**
- Double-check the Web Client ID in `GoogleSignInButton.android.kt`
- Make sure it's the Web Client ID, not Android Client ID

**Sign-in doesn't start**
- Check that `play-services-auth` dependency is included
- Verify your device has Google Play Services

### iOS Issues

**Sign-in button doesn't work**
- iOS implementation needs to be completed
- Follow iOS setup steps above
- Implement the native GIDSignIn flow

## Security Notes

1. **Never commit your `google-services.json` or `GoogleService-Info.plist` to public repositories**
2. Add these files to `.gitignore`
3. Use environment variables or secure secret management for production
4. Rotate your OAuth client secrets periodically

## Additional Resources

- [Firebase Google Sign-In Documentation](https://firebase.google.com/docs/auth/android/google-signin)
- [Google Sign-In Android Integration](https://developers.google.com/identity/sign-in/android/start-integrating)
- [Google Sign-In iOS Integration](https://developers.google.com/identity/sign-in/ios/start-integrating)
