import React, { useEffect, useState } from "react";
import { FaEdit, FaTrash, FaPlus } from "react-icons/fa"; // Importing react-icons
import Layout from "../component/Layout";
import ApiService from "../service/ApiService";

const CategoryPage = () => {
  const [categories, setCategories] = useState([]);
  const [categoryName, setCategoryName] = useState("");
  const [message, setMessage] = useState("");
  const [isEditing, setIsEditing] = useState(false);
  const [editingCategoryId, setEditingCategoryId] = useState(null);

  // Fetch the categories from our backend
  useEffect(() => {
    const getCategories = async () => {
      try {
        const response = await ApiService.getAllCategory();
        if (response.status === 200) {
          setCategories(response.categories);
        }
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error fetching categories: " + error
        );
      }
    };
    getCategories();
  }, []);

  // Add category
  const addCategory = async () => {
    if (!categoryName) {
      showMessage("Category name cannot be empty");
      return;
    }
    try {
      await ApiService.createCategory({ name: categoryName });
      showMessage("Category successfully added");
      setCategoryName(""); // Clear input
      window.location.reload(); // Reload page
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error adding category: " + error
      );
    }
  };

  // Edit category
  const editCategory = async () => {
    try {
      await ApiService.updateCategory(editingCategoryId, {
        name: categoryName,
      });
      showMessage("Category successfully updated");
      setIsEditing(false);
      setCategoryName(""); // Clear input
      window.location.reload(); // Reload page
    } catch (error) {
      showMessage(
        error.response?.data?.message || "Error updating category: " + error
      );
    }
  };

  // Populate the edit category data
  const handleEditCategory = (category) => {
    setIsEditing(true);
    setEditingCategoryId(category.id);
    setCategoryName(category.name);
  };

  // Delete category
  const handleDeleteCategory = async (categoryId) => {
    if (window.confirm("Are you sure you want to delete this category?")) {
      try {
        await ApiService.deleteCategory(categoryId);
        showMessage("Category successfully deleted");
        window.location.reload(); // Reload page
      } catch (error) {
        showMessage(
          error.response?.data?.message || "Error deleting category: " + error
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
      <div className="category-page">
        <div className="category-header">
          <h1>Categories</h1>
          <div className="add-cat">
            <input
              value={categoryName}
              type="text"
              placeholder="Category Name"
              onChange={(e) => setCategoryName(e.target.value)}
            />
            {!isEditing ? (
              <button onClick={addCategory}>
                <FaPlus /> Add Category
              </button>
            ) : (
              <button onClick={editCategory}>
                <FaEdit /> Edit Category
              </button>
            )}
          </div>
        </div>

        {categories && (
          <ul className="category-list">
            {categories.map((category) => (
              <li className="category-item" key={category.id}>
                <span>{category.name}</span>
                <div className="category-actions">
                  <button onClick={() => handleEditCategory(category)}>
                    <FaEdit /> Edit
                  </button>
                  <button onClick={() => handleDeleteCategory(category.id)}>
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

export default CategoryPage;



