import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { FaUser, FaEnvelope, FaLock, FaPhone, FaKey } from "react-icons/fa"; // Importing react-icons
import ApiService from "../service/ApiService";

const RegisterPage = () => {
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [phoneNumber, setPhoneNumber] = useState("");
  const [role, setRole] = useState("MANAGER"); // Default role is MANAGER
  const [adminCode, setAdminCode] = useState(""); // Admin code for verification
  const [message, setMessage] = useState("");

  const navigate = useNavigate();

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const registerData = { name, email, password, phoneNumber, role };
      
      if (role === "ADMIN" && adminCode !== "789308") {
        showMessage("Invalid admin code");
        return;
      }

      await ApiService.registerUser(registerData);
      setMessage("Registration Successful");

      if (role === "ADMIN") {
        navigate("/admin-dashboard");
      } else if (role === "MANAGER") {
        navigate("/manager-dashboard");
      }
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error Registering a User: " + error
      );
      console.log(error);
    }
  };

  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <div className="auth-container">
      <h2>Register</h2>

      {message && <p className="message">{message}</p>}

      <form onSubmit={handleRegister}>
        <div className='input-container'>
          <FaUser />
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
        </div>

        <div className='input-container'>
          <FaEnvelope />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
        </div>

        <div className='input-container'>
          <FaLock />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
        </div>

        <div className='input-container'>
          <FaPhone />
          <input
            type="text"
            placeholder="Phone Number"
            value={phoneNumber}
            onChange={(e) => setPhoneNumber(e.target.value)}
            required
          />
        </div>

        <div>
          <label>
            Role:
            <select value={role} onChange={(e) => setRole(e.target.value)} required>
              <option value="MANAGER">Manager</option>
              <option value="ADMIN">Admin</option>
            </select>
          </label>
        </div>

        {role === "ADMIN" && (
          <div className='input-container'>
            <FaKey />
            <input
              type="text"
              placeholder="Enter Admin Code"
              value={adminCode}
              onChange={(e) => setAdminCode(e.target.value)}
              required
            />
          </div>
        )}

        <button type="submit">Register</button>
      </form>
      <p>Already have an account? <a href="/login">Login</a></p>
    </div>
  );
};

export default RegisterPage;



