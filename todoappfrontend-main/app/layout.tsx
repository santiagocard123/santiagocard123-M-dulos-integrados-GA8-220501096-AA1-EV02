import type { Metadata } from "next";
import localFont from "next/font/local";
import "./globals.css";

const geistSans = localFont({
  src: "./fonts/GeistVF.woff",
  variable: "--font-geist-sans",
  weight: "100 900",
});
const geistMono = localFont({
  src: "./fonts/GeistMonoVF.woff",
  variable: "--font-geist-mono",
  weight: "100 900",
});

export const metadata: Metadata = {
  title: "TodoApp",
  description: "App to manage tasks",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en" className=" bg-gray-900">
      <body
        className={`${geistSans.variable} ${geistMono.variable} antialiased`}
      >
        {children}
      </body>
    </html>
  );
}


graph TD
    %% Frontend
    A[Frontend] -->|Interacts with| B[Backend]
    B -->|Manages Data| C[API Server]
    C -->|Handles Requests| D[UserController]
    D -->|Validates| E[TodoListController]
    E -->|Performs CRUD| F[TodoService]
    F -->|Accesses Database| G[MongoDB]

    subgraph Frontend
        A1[App] --> A2[Login]
        A1 --> A3[Register]
        A1 --> A4[Sidebar]
        A1 --> A5[Main]
    end

    subgraph Backend
        B1[Controller] --> B2[UserController]
        B1 --> B3[TodoListController]
        B1 --> B4[TaskController]
        B2 --> B5[TodoService]
        B3 --> B5[TodoService]
        B5 --> B6[TodoRepository]
    end
