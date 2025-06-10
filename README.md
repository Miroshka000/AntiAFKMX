# 🎮 AntiAFK Plugin for Nukkit

🔍 A powerful AFK detection plugin that automatically identifies and kicks inactive players on your Nukkit Minecraft server!

## ✨ Features

- 🔎 **Automatic AFK Detection** - Detects when players stop moving, chatting, or interacting
- 👋 **AFK Kick System** - Removes inactive players after a configurable time period
- ⚠️ **Warning System** - Alerts players before they get kicked
- 📢 **Broadcast Messages** - Notifies server when players go AFK or return
- 🛡️ **Permission-Based Bypass** - Exempt specific players or groups from AFK detection
- 🔧 **Fully Configurable** - Customize all timers, messages, and behaviors

## 📋 Commands and Permissions

- 🔑 `antiafk.bypass` - Players with this permission will not be marked as AFK or kicked

## ⚙️ Configuration

```yaml
# Time in minutes after which a player is considered AFK
afk-time: 1

# Time in minutes after which an AFK player will be kicked
kick-time: 1

# Warning message sent to player before kick (in minutes)
warning-time: 1

# Whether to broadcast a message when a player goes AFK
broadcast-afk: true

# Messages are fully customizable in config.yml
```

## 💻 Installation

1. Download the latest release
2. Place the .jar file in your plugins folder
3. Restart your server
4. Configure settings in config.yml as needed

## 🌟 Why Choose AntiAFK?

- 🚀 **Performance Friendly** - Minimal impact on server resources
- 🧠 **Smart Detection** - Monitors multiple types of player activity
- 🛠️ **Easy Setup** - Works great out of the box with sensible defaults
- 📝 **Customizable** - Tailor the plugin to fit your server's needs

## 🔄 Version Information

Current version: 1.0
Nukkit API: 1.0.0

## 📱 Contact

- Telegram: https://t.me/ForgePlugins
- VKontakte: https://vk.com/forgeplugin
- GitHub: https://github.com/Miroshka000 