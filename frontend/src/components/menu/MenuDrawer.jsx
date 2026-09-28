import { useEffect } from 'react';
import './MenuDrawer.css';

export default function MenuDrawer({ open, onClose, user }) {
  useEffect(() => {
    if (!open) return undefined;
    const handleKeyDown = (e) => {
      if (e.key === 'Escape') onClose();
    };
    document.addEventListener('keydown', handleKeyDown);
    return () => document.removeEventListener('keydown', handleKeyDown);
  }, [open, onClose]);

  return (
    <>
      <div
        className={`menu-drawer-backdrop ${open ? 'menu-drawer-backdrop--open' : ''}`}
        onClick={onClose}
        aria-hidden="true"
      />
      <div className={`menu-drawer ${open ? 'menu-drawer--open' : ''}`}>
        <nav className="menu-drawer__nav">
          <a className="menu-drawer__link" href="#">Home</a>
          <a className="menu-drawer__link" href="#">Orders</a>
          <a className="menu-drawer__link" href="#">Settings</a>
          <a className="menu-drawer__link" href="#">Help</a>
        </nav>
        {user && (
          <div className="menu-drawer__account">
            <p className="menu-drawer__account-name">{user.name} {user.lastName}</p>
            <p className="menu-drawer__account-company">{user.companyName}</p>
          </div>
        )}
      </div>
    </>
  );
}
