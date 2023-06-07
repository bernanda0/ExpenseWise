import React, { useState } from 'react';

const Transaction = () => {
  const [data, setData] = useState([
    { id: 1, name: 'Item 1', editable: false },
    { id: 2, name: 'Item 2', editable: false },
    { id: 3, name: 'Item 3', editable: false },
  ]);

  const handleDelete = (id) => {
    setData(data.filter((item) => item.id !== id));
  };

  const handleEdit = (id) => {
    setData((prevData) =>
      prevData.map((item) =>
        item.id === id ? { ...item, editable: true } : item
      )
    );
  };

  const handleNameChange = (id, event) => {
    const newName = event.target.value;
    setData((prevData) =>
      prevData.map((item) =>
        item.id === id ? { ...item, name: newName } : item
      )
    );
  };

  const handleSave = (id) => {
    setData((prevData) =>
      prevData.map((item) =>
        item.id === id ? { ...item, editable: false } : item
      )
    );
  };

  const handleAdd = () => {
    const newItem = {
      id: data.length + 1,
      name: `Item ${data.length + 1}`,
      editable: false,
    };
    setData([...data, newItem]);
  };

  return (
    <div>
      <table className="pt-10 min-w-full bg-white rounded-lg">
        <thead>
          <tr>
            <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
              ID
            </th>
            <th className="px-6 py-3 border-b-2 border-gray-300 text-left leading-4 text-blue-500 tracking-wider">
              Name
            </th>
            <th className="px-6 py-3 border-b-2 border-gray-300"></th>
          </tr>
        </thead>
        <tbody>
          {data.map((item) => (
            <tr key={item.id}>
              <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-300">
                {item.id}
              </td>
              <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-300">
                {item.editable ? (
                  <input
                    className="border border-gray-300 px-2 py-1 rounded focus:outline-none"
                    type="text"
                    value={item.name}
                    onChange={(event) => handleNameChange(item.id, event)}
                  />
                ) : (
                  item.name
                )}
              </td>
              <td className="px-6 py-4 whitespace-no-wrap border-b border-gray-300">
                {item.editable ? (
                  <button
                    className="px-4 py-2 mr-2 text-sm font-medium text-green-600 bg-transparent border border-green-600 rounded hover:bg-green-600 hover:text-white"
                    onClick={() => handleSave(item.id)}
                  >
                    Save
                  </button>
                ) : (
                  <button
                    className="px-4 py-2 mr-2 text-sm font-medium text-blue-600 bg-transparent border border-blue-600 rounded hover:bg-blue-600 hover:text-white"
                    onClick={() => handleEdit(item.id)}
                  >
                    Edit
                  </button>
                )}
                <button
                  className="px-4 py-2 mr-2 text-sm font-medium text-red-600 bg-transparent border border-red-600 rounded hover:bg-red-600 hover:text-white"
                  onClick={() => handleDelete(item.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
      <button
        className="bg-white px-4 py-2 mt-4 text-sm font-medium text-green-600 bg-transparent border border-green-600 rounded hover:bg-green-600 hover:text-white"
        onClick={handleAdd}
      >
        Add
      </button>
    </div>
  );
};

export default Transaction;

