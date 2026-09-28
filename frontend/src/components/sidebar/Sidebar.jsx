import BuyerCategoryPanel from './BuyerCategoryPanel';
import SellerActionPanel from './SellerActionPanel';
import StatusMessage from '../common/StatusMessage';
import './Sidebar.css';

export default function Sidebar({ isBuyer, isSupplier }) {
  return (
    <aside className="sidebar">
      {isBuyer && isSupplier && (
        <>
          <BuyerCategoryPanel className="sidebar__half" />
          <div className="sidebar__divider" />
          <SellerActionPanel className="sidebar__half" />
        </>
      )}
      {isBuyer && !isSupplier && <BuyerCategoryPanel className="sidebar__full" />}
      {!isBuyer && isSupplier && <SellerActionPanel className="sidebar__full" />}
      {!isBuyer && !isSupplier && (
        <StatusMessage tone="empty">No sidebar options available.</StatusMessage>
      )}
    </aside>
  );
}
