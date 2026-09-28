import './SellerActionPanel.css';

const ACTIONS = ['View my items', 'View statistics', 'Modify items'];

export default function SellerActionPanel({ className = '' }) {
  // TODO: wire to real routes once those views exist
  const handleClick = () => {};

  return (
    <div className={`seller-action-panel ${className}`}>
      <h2 className="seller-action-panel__title">Seller tools</h2>
      <ul className="seller-action-panel__list">
        {ACTIONS.map((action) => (
          <li key={action}>
            <button type="button" className="seller-action-panel__item" onClick={handleClick}>
              {action}
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
}
