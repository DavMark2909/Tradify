import { MenuIcon, CloseIcon } from '../icons/Icons';
import './MenuButton.css';

export default function MenuButton({ onClick, isOpen }) {
  return (
    <button
      type="button"
      className="menu-button"
      onClick={onClick}
      aria-expanded={isOpen}
      aria-label="Toggle menu"
    >
      {isOpen ? <CloseIcon size={20} /> : <MenuIcon size={20} />}
    </button>
  );
}
