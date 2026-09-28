import SearchBar from '../search/SearchBar';
import MenuButton from '../menu/MenuButton';
import './Header.css';

export default function Header({ onToggleMenu, onSearch, isMenuOpen }) {
  return (
    <header className="header">
      <span className="header__brand">Tradify</span>
      <div className="header__search">
        <SearchBar onSearch={onSearch} />
      </div>
      <MenuButton onClick={onToggleMenu} isOpen={isMenuOpen} />
    </header>
  );
}
