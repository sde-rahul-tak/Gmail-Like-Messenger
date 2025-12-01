# Simple Gmail_Messanger System in C++

## Overview
We have created a basic version of a  Gmail-like email system using C++. The system is designed to handle multiple user accounts and provide basic email functionalities such as sending, receiving, and managing messages. The core data structures used include a doubly linked list for storing user accounts and singly linked lists for managing sent and received messages per user.

## Key Features and Functionalities

### User Account Management
- **Account Creation:** Users can create new accounts with unique identifiers.
- **Login/Logout:** Users can securely log in to their accounts to access their emails and log out when done.
- **Password Management:** Users have the option to change their account passwords for security purposes.
- **Account Deletion:** Users can permanently delete their accounts, along with all associated data.

### Message Handling
- **Sending Messages:** Logged-in users can send plain text messages to other users within the system.
- **Receiving Messages:** Users can view messages received from other users, organized in a singly linked list.
- **Message Management:** Users can perform various actions on their messages, including reading, deleting, and marking them as important (starred).
- **Trash and Recovery:** Deleted messages are moved to a trash vector, where they can be viewed or permanently deleted. However, once messages are deleted from the trash, they cannot be recovered.

### Message Search and Interaction
- **Sequential Search:** Users can search for specific messages based on the sender or recipient. The system uses sequential search to locate these messages and stores references to them in vectors for easy access.
- **Message Actions:** Users can interact with the search results, such as reading the full content of the messages, deleting them, or changing their importance status (starred or unstarred).

### Interface and Usability
- **User Interface:** The system provides a command-line interface for users to navigate through their accounts and manage their messages.
- **Feedback Mechanisms:** The system provides feedback on actions such as successful account creation, message sending, and deletion operations.

## Limitations
- **Text-Only Messages:** Currently, the system supports only plain text messages, without attachments or multimedia.
- **Single-Session Login:** Users can only be logged into one account at a time. There is no support for simultaneous multi-account sessions.
- **Irretrievable Deleted Messages:** Once messages are permanently deleted from the trash, they cannot be recovered, which could be a limitation for users who might accidentally delete important information.

## Future Scope and Enhancements
- **Enhanced Message Features:** Adding support for attachments, multimedia messages, and formatting options would bring the system closer to full-fledged email services.
- **Multi-Session Support:** Implementing the ability for users to be logged into multiple accounts simultaneously would enhance usability for those managing multiple email addresses.
- **Advanced Search and Filters:** Improving the search functionality with filters based on date, subject, and other criteria would provide users with more efficient ways to manage their emails.
- **Security Enhancements:** Additional security measures, such as two-factor authentication and encryption, could be integrated to enhance user privacy and data protection.
- **User Interface Improvements:** Developing a graphical user interface (GUI) would make the system more user-friendly and accessible to a broader audience.
