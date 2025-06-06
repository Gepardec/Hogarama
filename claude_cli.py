#!/usr/bin/env python3
import anthropic
import os
import sys
import readline  # Enables arrow key navigation and command history

# Set your API key - either directly or via environment variable
# It's recommended to use environment variables for security
API_KEY = os.environ.get("ANTHROPIC_API_KEY")
if not API_KEY:
    # Fallback to hardcoded key (not recommended for production)
    API_KEY = "your-api-key-here"  # Replace with your actual API key

# Initialize the Anthropic client
client = anthropic.Anthropic(api_key=API_KEY)

# ANSI color codes for prettier output
COLORS = {
    "blue": "\033[94m",
    "green": "\033[92m",
    "yellow": "\033[93m",
    "red": "\033[91m",
    "bold": "\033[1m",
    "end": "\033[0m"
}

def print_colored(text, color="blue", bold=False):
    """Print text with color"""
    color_code = COLORS[color]
    bold_code = COLORS["bold"] if bold else ""
    print(f"{bold_code}{color_code}{text}{COLORS['end']}")

def print_banner():
    """Print a welcome banner"""
    print_colored("\n" + "=" * 60, "blue", True)
    print_colored("                CLAUDE CLI TERMINAL", "green", True)
    print_colored("=" * 60, "blue", True)
    print_colored("Type your messages to chat with Claude.", "yellow")
    print_colored("Use /help for commands, /quit to exit.", "yellow")
    print_colored("=" * 60 + "\n", "blue", True)

def print_help():
    """Print help information"""
    print_colored("\nAvailable Commands:", "green", True)
    print_colored("  /help  - Show this help message", "yellow")
    print_colored("  /clear - Clear conversation history", "yellow")
    print_colored("  /quit  - Exit the application", "yellow")
    print_colored("  /save filename - Save conversation to file", "yellow")
    print("\n")

def main():
    print_banner()

    # Initialize conversation history
    conversation = []

    # Main conversation loop
    while True:
        try:
            # Get user input with a prompt
            user_input = input(f"{COLORS['green']}You:{COLORS['end']} ")

            # Process commands
            if user_input.lower() == "/quit" or user_input.lower() == "exit":
                print_colored("Goodbye!", "blue")
                break

            elif user_input.lower() == "/help":
                print_help()
                continue

            elif user_input.lower() == "/clear":
                conversation = []
                print_colored("Conversation history cleared.", "yellow")
                continue

            elif user_input.lower().startswith("/save "):
                filename = user_input[6:].strip()
                with open(filename, "w") as f:
                    for message in conversation:
                        f.write(f"{message['role'].upper()}: {message['content']}\n\n")
                print_colored(f"Conversation saved to {filename}", "yellow")
                continue

            # Add user message to conversation history
            conversation.append({"role": "user", "content": user_input})

            # Get response from Claude
            print_colored("Claude is thinking...", "blue")
            try:
                response = client.messages.create(
                    model="claude-3-7-sonnet-20250219",
                    max_tokens=1000,
                    messages=conversation
                )

                # Extract Claude's response
                claude_response = response.content[0].text

                # Print Claude's response
                print(f"{COLORS['blue']}Claude:{COLORS['end']} {claude_response}\n")

                # Add Claude's response to conversation history
                conversation.append({"role": "assistant", "content": claude_response})

            except Exception as e:
                print_colored(f"Error: {str(e)}", "red")

        except KeyboardInterrupt:
            print_colored("\nCtrl+C detected. Exiting...", "yellow")
            break

        except Exception as e:
            print_colored(f"An error occurred: {str(e)}", "red")

if __name__ == "__main__":
    main()