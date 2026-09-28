import { useEffect, useState } from 'react';
import { SearchIcon } from '../icons/Icons';
import './SearchBar.css';

export default function SearchBar({ onSearch, placeholder = 'Search products…' }) {
  const [value, setValue] = useState('');

  useEffect(() => {
    const timeoutId = setTimeout(() => onSearch(value), 250);
    return () => clearTimeout(timeoutId);
  }, [value, onSearch]);

  return (
    <div className="search-bar">
      <SearchIcon size={18} className="search-bar__icon" />
      <input
        type="text"
        className="search-bar__input"
        placeholder={placeholder}
        value={value}
        onChange={(e) => setValue(e.target.value)}
      />
    </div>
  );
}
