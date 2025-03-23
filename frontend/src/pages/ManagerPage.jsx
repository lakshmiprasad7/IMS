import React, { useEffect, useState } from "react";
import { FaEdit, FaTrash } from "react-icons/fa"; // Importing react-icons
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";

const ManagerPage = () => {
  const [managers, setManagers] = useState([]);
  const [managerName, setManagerName] = useState("");
  const [message, setMessage] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editingManagerId, setEditingManagerId] = useState(null);

  // Fetch the managers from our backend
  useEffect(() => {
    const getManagers = async () => {
      try {
        const response = await ApiService.getAllUsers();
        if (response.status === 200) {
          const managerList = response.users.filter(user => user.role === "MANAGER");
          setManagers(managerList);
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error fetching managers: " + error
        );
      }
    };
    getManagers();
  }, []);

  // Edit manager
  const editManager = async () => {
    try {
      await ApiService.updateUser(editingManagerId, {
        name: managerName,
      });
      showMessage("Manager successfully updated");
      setIsEditing(false);
      setManagerName(""); // Clear input
      window.location.reload(); // Reload page
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error updating manager: " + error
      );
    }
  };

  // Populate the edit manager data
  const handleEditManager = (manager) => {
    setIsEditing(true);
    setEditingManagerId(manager.id);
    setManagerName(manager.name);
  };

  // Delete manager
  const handleDeleteManager = async (managerId) => {
    if (window.confirm("Are you sure you want to delete this manager?")) {
      try {
        await ApiService.deleteUser(managerId);
        showMessage("Manager successfully deleted");
        window.location.reload(); // Reload page
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error deleting manager: " + error
        );
      }
    }
  };

  // Method to show message or errors
  const showMessage = (msg) => {
    setMessage(msg);
    setTimeout(() => {
      setMessage("");
    }, 4000);
  };

  return (
    <Layout>
      {message && <div className="message">{message}</div>}
      <div className="manager-page">
        <div className="manager-header">
          <h1>Managers</h1>
          {isEditing && (
            <div className="edit-manager">
              <input
                value={managerName}
                type="text"
                placeholder="Manager Name"
                onChange={(e) => setManagerName(e.target.value)}
              />
              <button onClick={editManager}>
                <FaEdit /> Edit Manager
              </button>
            </div>
          )}
        </div>

        {managers && (
          <ul className="manager-list">
            {managers.map((manager) => (
              <li className="manager-item" key={manager.id}>
                <span>{manager.name}</span>
                <div className="manager-actions">
                  <button onClick={() => handleEditManager(manager)}>
                    <FaEdit /> Edit
                  </button>
                  <button onClick={() => handleDeleteManager(manager.id)}>
                    <FaTrash /> Delete
                  </button>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>
    </Layout>
  );
};

export default ManagerPage;
